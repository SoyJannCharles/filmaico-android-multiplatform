package com.jycra.filmaico.core.network

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jycra.filmaico.core.network.di.AuthHttpClient
import com.jycra.filmaico.data.stream.data.service.ScrapingService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScrapingServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @AuthHttpClient private val authHttpClient: OkHttpClient
) : ScrapingService {

    @SuppressLint("SetJavaScriptEnabled")
    override fun extractStreamUri(iframeUrl: String): Flow<String> = callbackFlow {

        val webView = WebView(context)

        withContext(Dispatchers.Main) {

            webView.apply {

                settings.javaScriptEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                webViewClient = object : WebViewClient() {

                    override fun shouldInterceptRequest(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): WebResourceResponse? {
                        request?.url?.toString()?.let { url ->

                            val urlWithoutParams = url.split("?").first()
                            val isTargetM3u8 = urlWithoutParams.endsWith(".m3u8") && urlWithoutParams.contains("master.m3u8") ||
                                    urlWithoutParams.endsWith(".txt") && urlWithoutParams.contains("master.txt")

                            if (isTargetM3u8) {
                                trySend(url)
                            }

                            if (urlWithoutParams.endsWith(".mp4") && url != iframeUrl) {
                                trySend(url)
                            }

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

        }

        awaitClose {
            Handler(Looper.getMainLooper()).post {
                try {
                    webView.destroy()
                } catch (e: Exception) {

                }
            }
        }

    }.flowOn(Dispatchers.Main)

    override suspend fun extractStreamUrl(scrapeUrl: String, regexPattern: String, headers: Map<String, String>?): String? {
        val htmlContent = downloadHtml(scrapeUrl, headers)
        if (htmlContent.isNullOrEmpty()) {
            return null
        }
        return extractVideoUrlFromHtml(htmlContent, regexPattern)
    }

    private suspend fun downloadHtml(url: String, headers: Map<String, String>?): String? {
        return withContext(Dispatchers.IO) {

            try {

                val requestBuilder = Request.Builder().url(url)

                headers?.forEach { (key, value) ->
                    requestBuilder.header(key, value)
                }
                val request = requestBuilder.build()

                authHttpClient.newCall(request).execute().use { response ->

                    if (response.isSuccessful) {

                        val htmlContent = response.body.string()
                        if (!htmlContent.isEmpty()) htmlContent else null

                    } else {
                        null
                    }

                }

            } catch (e: Exception) {
                null
            }

        }

    }

    private fun extractVideoUrlFromHtml(htmlContent: String, regexPattern: String): String? {

        val regex = Regex(regexPattern)
        val matchResult = regex.find(htmlContent)

        return matchResult?.groupValues?.getOrNull(1)?.replace("\\/", "/")

    }

}