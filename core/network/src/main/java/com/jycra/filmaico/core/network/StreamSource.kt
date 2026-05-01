package com.jycra.filmaico.core.network

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jycra.filmaico.core.firebase.model.stream.CookieDto
import com.jycra.filmaico.core.firebase.model.stream.KeysDto
import com.jycra.filmaico.core.network.api.StreamApi
import com.jycra.filmaico.core.network.di.XAuthHttpClient
import com.jycra.filmaico.data.stream.data.service.StreamService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.net.URI
import javax.inject.Inject
import kotlin.coroutines.resume

class StreamSource @Inject constructor(
    private val streamApi: StreamApi,
    @ApplicationContext private val context: Context,
    @XAuthHttpClient private val client: OkHttpClient,
    private val engine: StreamExtractionEngine
) : StreamService {

    override fun resolveUrlFromWebView(iframeUrl: String): Flow<String> = callbackFlow {

        var isFlowClosed = false
        var webView: WebView? = null

        val job = launch(Dispatchers.Main) {

            try {

                val staticUrl = withContext(Dispatchers.IO) {
                    engine.extract(iframeUrl)
                }

                if (staticUrl != null) {
                    if (!isFlowClosed) {
                        trySend(staticUrl)
                        close()
                    }
                    return@launch
                }

                webView = WebView(context).apply {

                    willNotDraw()

                    settings.apply {

                        javaScriptEnabled = true
                        mediaPlaybackRequiresUserGesture = false

                        loadsImagesAutomatically = false
                        blockNetworkImage = true

                        domStorageEnabled = true
                        databaseEnabled = false

                        setSupportZoom(false)

                    }

                    webViewClient = object : WebViewClient() {
                        override fun shouldInterceptRequest(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): WebResourceResponse? {

                            val url = request?.url?.toString() ?: return null

                            val clean = url.substringBefore("?")
                            val lowerClean = clean.lowercase()

                            val hasVideoExtension = lowerClean.contains(".m3u8") ||
                                    lowerClean.contains(".txt") ||
                                    lowerClean.contains(".wolf") ||
                                    lowerClean.contains(".m3u")

                            val isPlaylist = lowerClean.contains("master") ||
                                    lowerClean.contains("index") ||
                                    lowerClean.contains("playlist") ||
                                    lowerClean.contains("global")

                            if (hasVideoExtension && isPlaylist) {

                                if (!isFlowClosed) {
                                    trySend(url)
                                }

                                view?.post { view.stopLoading() }

                            }

                            return super.shouldInterceptRequest(view, request)

                        }
                    }

                    loadUrl(iframeUrl)

                }

            } catch (e: Exception) {
                close(e)
            }

        }

        awaitClose {

            isFlowClosed = true

            Handler(Looper.getMainLooper()).post {
                webView?.apply {
                    stopLoading()
                    loadUrl("about:blank")
                    destroy()
                }
                webView = null
                job.cancel()
            }

        }

    }

    override fun fetchHlsManifest(
        url: String,
        includeChildren: Boolean
    ): Flow<Triple<String, String, String>> = flow {

        val (masterFinalUrl, masterContent) = downloadString(url) ?: return@flow
        emit(Triple(url, masterFinalUrl, masterContent))

        if (!includeChildren) return@flow

        val childUrls = extractChildPlaylists(masterFinalUrl, masterContent)
        if (childUrls.isEmpty()) return@flow

        coroutineScope {

            val results = childUrls.map { childUrl ->
                async {
                    ensureActive()
                    downloadString(childUrl)?.let { Triple(childUrl, it.first, it.second) }
                }
            }.awaitAll().filterNotNull()

            results.forEach { emit(it) }

        }

    }.flowOn(Dispatchers.IO)

    private suspend fun downloadString(url: String): Pair<String, String>? =
        suspendCancellableCoroutine { cont ->

            val request = Request.Builder().url(url).build()
            val call = client.newCall(request)

            cont.invokeOnCancellation {
                call.cancel()
            }

            call.enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    if (!cont.isCancelled) cont.resume(null)
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        val body = it.body?.string()
                        if (it.isSuccessful && body != null) {
                            val finalUrl = it.request.url.toString()
                            cont.resume(Pair(finalUrl, body))
                        } else {
                            cont.resume(null)
                        }
                    }
                }
            })

        }


    private fun extractChildPlaylists(baseUrl: String, manifestContent: String): List<String> {

        val childUrls = mutableSetOf<String>()
        val baseUri = URI(baseUrl)

        manifestContent.lines().forEach { line ->

            val trimmed = line.trim()

            if (trimmed.isEmpty()) return@forEach

            if (!trimmed.startsWith("#")) {

                childUrls.add(baseUri.resolve(trimmed).toString())

            } else if (trimmed.startsWith("#EXT-X-MEDIA")) {

                val uriRegex = """URI="([^"]+)"""".toRegex()

                val match = uriRegex.find(trimmed)
                if (match != null) {
                    val extractedUri = match.groupValues[1]
                    childUrls.add(baseUri.resolve(extractedUri).toString())
                }

            }

        }

        return childUrls.toList()

    }


    override suspend fun fetchJwtToken(url: String): String {

        val response = streamApi.getJwt(url)

        if (!response.isSuccessful) {
            throw Exception("Error HTTP JWT principal: ${response.code()}")
        }

        val body = response.body()?.string()
            ?: throw Exception("JWT response body es null")

        return body

    }

    override suspend fun fetchCdnToken(
        url: String,
        authorization: String,
        origin: String,
        referer: String
    ): String {

        val response = streamApi.getCdnToken(
            url = url,
            authorization = authorization,
            origin = origin,
            referer = referer
        )

        if (!response.isSuccessful) {
            throw Exception("CDN token request failed: ${response.code()}")
        }

        val body = response.body()?.string()
            ?: throw Exception("CDN token response body es null")

        return body

    }

    override suspend fun fetchCookies(url: String): List<CookieDto> {
        return streamApi.getCookies(url)
    }

    override suspend fun fetchDrmKeys(
        url: String,
        userAgent: String,
        payload: String
    ): KeysDto {

        val mediaType = "application/octet-stream".toMediaTypeOrNull()
        val requestBody = payload.toRequestBody(mediaType)

        return streamApi.getDrmKeys(url, userAgent, requestBody)

    }

}