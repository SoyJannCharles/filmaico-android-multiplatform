package com.jycra.filmaico.core.network

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jycra.filmaico.core.model.stream.CookieDto
import com.jycra.filmaico.core.model.stream.KeysDto
import com.jycra.filmaico.core.network.api.StreamApi
import com.jycra.filmaico.core.network.di.XAuthHttpClient
import com.jycra.filmaico.data.stream.data.service.StreamService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.ProducerScope
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
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume

class StreamNetworkSource @Inject constructor(
    private val streamApi: StreamApi,
    @ApplicationContext private val context: Context,
    @XAuthHttpClient private val client: OkHttpClient,
    private val engine: StreamExtractionEngine
) : StreamService {

    override fun resolveUrlFromWebView(iframeUrl: String): Flow<String> = callbackFlow {

        val job = Job()
        val scope = CoroutineScope(Dispatchers.IO + job)

        var webView: WebView? = null

        scope.launch {

            try {

                ensureActive()

                val staticUrl = engine.extract(iframeUrl)

                ensureActive()

                if (staticUrl != null) {
                    trySend(staticUrl)
                    close()
                    return@launch
                }

                withContext(Dispatchers.Main) {
                    webView = runWebViewFallback(iframeUrl, this@callbackFlow)
                }

            } catch (e: CancellationException) {
                Log.d("StreamNetworkSource", "Cancelado scraping")
            }

        }

        awaitClose {

            Log.d("StreamNetworkSource", "Cerrando flujo")

            job.cancel()

            webView?.stopLoading()
            webView?.destroy()
            webView = null

        }

    }.flowOn(Dispatchers.IO)

    @SuppressLint("SetJavaScriptEnabled")
    private fun runWebViewFallback(iframeUrl: String, scope: ProducerScope<String>): WebView {

        val webView = WebView(context)

        webView.apply {

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

                    val isTarget =
                        (clean.endsWith(".m3u8") && clean.contains("master")) ||
                                (clean.endsWith(".txt") && clean.contains("master"))

                    if (isTarget) {
                        scope.trySend(url)
                        view?.post { view.stopLoading() }
                    }

                    return super.shouldInterceptRequest(view, request)

                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val url = request?.url.toString()
                    return url.startsWith("intent://") || url.startsWith("market://")
                }

            }

            loadUrl(iframeUrl)

        }

        return webView

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