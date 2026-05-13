package com.jycra.filmaico.core.network.resolver

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jycra.filmaico.core.common.logger.FLog
import com.jycra.filmaico.core.common.logger.LogCategory
import com.jycra.filmaico.core.network.util.PackerUnpacker
import com.jycra.filmaico.core.network.di.XAuthHttpClient
import com.jycra.filmaico.data.stream.resolver.IframeResolver
import com.jycra.filmaico.data.stream.util.extension.toHostOnly
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class DefaultIframeResolver @Inject constructor(
    @ApplicationContext private val context: Context,
    @XAuthHttpClient private val client: OkHttpClient,
    private val packerUnpacker: PackerUnpacker
) : IframeResolver {

    private var lastResolvedDomain: String? = null
    override fun getLastResolvedDomain(): String? = lastResolvedDomain

    override suspend fun resolveStatic(
        iframeUrl: String,
        preloadedHtml: String?
    ): String? = withContext(Dispatchers.IO) {

        try {

            val html = preloadedHtml ?: downloadHtml(iframeUrl) ?: return@withContext null

            val resolvedUrl = packerUnpacker.extractM3U8(html)

            if (resolvedUrl != null) {
                FLog.d(LogCategory.SCRAPER, "Static Resolution SUCCESS: Found m3u8 via Unpacker")
                return@withContext resolvedUrl
            }

            val directUrl = Regex("https?://[^\"']+\\.m3u8[^\"']*")
                .find(html)
                ?.value
                ?.replace("\\/", "/")

            if (directUrl != null) {
                FLog.d(LogCategory.SCRAPER, "Static Resolution SUCCESS: Found direct m3u8 in HTML")
                return@withContext directUrl
            }

            FLog.w(LogCategory.SCRAPER, "Static Resolution FAILED: No m3u8 found in HTML content")
            null

        } catch (e: Exception) {
            FLog.e(LogCategory.SCRAPER, "Error during static resolution", e)
            null
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun resolveWithWebView(iframeUrl: String): Flow<String> = callbackFlow {

        var isFlowClosed = false
        var webView: WebView? = null

        val job = launch(Dispatchers.Main) {

            try {
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

                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                            val newUrl = request?.url?.toString()
                            if (newUrl?.contains("/e/") == true) {
                                lastResolvedDomain = newUrl.toHostOnly()
                                FLog.d(LogCategory.SCRAPER, "WebView Redirected to landing: $lastResolvedDomain")
                            }
                            return false
                        }

                        override fun shouldInterceptRequest(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): WebResourceResponse? {

                            val url = request?.url?.toString() ?: return null
                            val lowerUrl = url.lowercase().substringBefore("?")

                            val isVideoFile = lowerUrl.endsWith(".m3u8") ||
                                    lowerUrl.endsWith(".txt") ||
                                    lowerUrl.endsWith(".wolf")

                            val isStreamPlaylist = lowerUrl.contains("master") ||
                                    lowerUrl.contains("index") ||
                                    lowerUrl.contains("playlist")

                            if (isVideoFile && isStreamPlaylist) {

                                FLog.d(LogCategory.SCRAPER, "WebView SNIFFED: $url")

                                if (!isFlowClosed) {

                                    view?.post {
                                        val currentUrl = view.url
                                        if (currentUrl?.contains("/e/") == true) {
                                            lastResolvedDomain = currentUrl.toHostOnly()
                                            FLog.d(LogCategory.DATABASE, "Target domain confirmed: $lastResolvedDomain")
                                        }
                                    }

                                    trySend(url)

                                    isFlowClosed = true
                                    view?.post { view.stopLoading() }
                                    close()

                                }

                            }

                            if (url.contains("ads") || url.contains("track") || url.contains("telemetry")) {
                                return WebResourceResponse("text/plain", "UTF-8", null)
                            }

                            return super.shouldInterceptRequest(view, request)

                        }

                        override fun onPageFinished(view: WebView?, url: String?) {

                            if (url?.contains("/e/") == true) {
                                lastResolvedDomain = url.toHostOnly()
                            }

                            view?.evaluateJavascript(
                                "document.getElementsByTagName('video')[0].src",
                                { src ->
                                    if (!src.isNullOrBlank() && src != "null" && !isFlowClosed) {
                                        trySend(src.replace("\"", ""))
                                        isFlowClosed = true
                                        close()
                                    }
                                })

                        }

                    }

                    loadUrl(iframeUrl)

                }

            } catch (e: Exception) {
                FLog.e(LogCategory.SCRAPER, "WebView Critical Error", e)
                close(e)
            }

        }

        awaitClose {

            isFlowClosed = true
            FLog.d(LogCategory.SCRAPER, "WebView Resources Cleaned Up")

            Handler(Looper.getMainLooper()).post {
                webView?.apply {
                    stopLoading()
                    loadUrl("about:blank")
                    clearCache(true)
                    destroy()
                }
                webView = null
                job.cancel()
            }

        }

    }

    private fun downloadHtml(url: String): String? {

        val request = Request.Builder()
            .url(url)
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) response.body.string() else null
            }
        } catch (e: Exception) {
            null
        }

    }

}