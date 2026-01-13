package com.jycra.filmaico.core.network

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
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
    override fun extractStreamM3u8Url(playerUrl: String): Flow<String> = callbackFlow {

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
                            //val isTargetM3u8 = url.endsWith(".m3u8") && !url.contains("master.m3u8")
                            val urlWithoutParams = url.split("?").first()
                            val isTargetM3u8 = urlWithoutParams.endsWith(".m3u8") && urlWithoutParams.contains("master.m3u8")
                            if (isTargetM3u8) {
                                Log.d("WebViewBlock", "Url M3u8: $url")
                                trySend(url)
                            }
                            if (urlWithoutParams.endsWith(".mp4") && url != playerUrl) {
                                Log.d("WebViewBlock", "Url Mp4: $url")
                                val headers = request.requestHeaders
                                Log.d("ScrapingService", "Headers capturados: $headers")
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

                        // Bloquea cualquier intento de abrir intent:// o market://
                        if (url.startsWith("intent://") || url.startsWith("market://")) {
                            // Opcional: log para debug
                            Log.d("WebViewBlock", "Bloqueada URL peligrosa: $url")
                            return true // <- bloquea
                        }

                        // Permite lo demás
                        return false
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        // Log para saber si la página terminó de cargar
                        Log.d("WebView", "onPageFinished: $url")
                    }
                }
                loadUrl(playerUrl)
            }
        }

        awaitClose {
            Handler(Looper.getMainLooper()).post {
                try {
                    webView.destroy()
                } catch (e: Exception) {
                    // Es buena idea loguear esta excepción si ocurre
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
        return withContext(Dispatchers.IO) { // Aseguramos que se ejecute en un hilo de fondo
            try {

                val requestBuilder = Request.Builder().url(url)

                headers?.forEach { (key, value) ->
                    requestBuilder.header(key, value)
                }
                val request = requestBuilder.build()

                authHttpClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        // --- CAMBIO CLAVE AQUÍ ---
                        // 1. Leemos el cuerpo UNA SOLA VEZ y lo guardamos en una variable.
                        val htmlContent = response.body?.string()

                        if (!htmlContent.isNullOrEmpty()) {
                            Log.i(
                                "ScrapingService",
                                "HTML descargado. Longitud: ${htmlContent.length} caracteres."
                            )
                            // 2. Devolvemos la variable que ya contiene el HTML.
                            htmlContent
                        } else {
                            Log.w(
                                "ScrapingService",
                                "La respuesta fue exitosa (código ${response.code}) pero el cuerpo está vacío."
                            )
                            null
                        }
                    } else {
                        Log.e(
                            "ScrapingService",
                            "Error al descargar el HTML. Código: ${response.code}"
                        )
                        null
                    }
                }
            } catch (e: Exception) {
                Log.i("ScrapingService", "Error al descargar el HTML: ${e.message}")
                null
            }
        }
    }

    private fun extractVideoUrlFromHtml(htmlContent: String, regexPattern: String): String? {
        val regex = Regex(regexPattern)
        val matchResult = regex.find(htmlContent)

        // groupValues[1] contiene el primer grupo de captura, que es lo que nos interesa
        return matchResult?.groupValues?.getOrNull(1)?.replace("\\/", "/")
    }

}