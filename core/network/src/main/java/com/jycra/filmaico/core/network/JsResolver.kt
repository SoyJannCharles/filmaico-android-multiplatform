package com.jycra.filmaico.core.network

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class JsResolver @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var webView: WebView? = null
    private val handler = Handler(Looper.getMainLooper())

    private var activeContinuation: CancellableContinuation<String?>? = null

    private var currentBaseUrl: String = ""

    init {

        handler.post {

            webView = WebView(context).apply {

                settings.javaScriptEnabled = true

                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun onUrlResolved(url: String) {

                        activeContinuation?.takeIf { it.isActive }?.resume(url.ifBlank { null })
                        activeContinuation = null

                        handler.post { resetEnvironment(currentBaseUrl) }

                    }
                }, "AndroidBridge")

            }

        }

    }

    private fun WebView.resetEnvironment(baseUrl: String) {
        currentBaseUrl = baseUrl
        loadDataWithBaseURL(baseUrl, "<html></html>", "text/html", "UTF-8", null)
    }

    @SuppressLint("SetJavaScriptEnabled")
    suspend fun resolve(jsContent: String, sourceUrl: String): String? =
        suspendCancellableCoroutine { continuation ->

            handler.post {

                val engine = webView
                if (engine == null) {
                    continuation.resume(null)
                    return@post
                }

                activeContinuation?.cancel()
                activeContinuation = continuation

                val uri = URI(sourceUrl)
                val targetBaseUrl = "${uri.scheme}://${uri.host}"

                val runner = """
                    (function() {
                        try {
                        
                            $jsContent 
                            
                            var result = "";
                            if (typeof finalURL !== 'undefined') result = finalURL;
                            else if (typeof destination !== 'undefined') result = destination;
                            
                            AndroidBridge.onUrlResolved(result);
                            
                        } catch(e) { 
                            AndroidBridge.onUrlResolved(""); 
                        }
                    })();
                """.trimIndent()

                val timeoutRunnable = Runnable {
                    if (continuation.isActive) {

                        continuation.resume(null)
                        activeContinuation = null

                        engine.resetEnvironment(currentBaseUrl)

                    }
                }

                handler.postDelayed(timeoutRunnable, 3000)

                if (currentBaseUrl == targetBaseUrl) {
                    engine.evaluateJavascript(runner, null)
                } else {
                    engine.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            engine.webViewClient = WebViewClient()
                            engine.evaluateJavascript(runner, null)
                        }
                    }
                    engine.resetEnvironment(targetBaseUrl)
                }

                continuation.invokeOnCancellation {

                    handler.removeCallbacks(timeoutRunnable)

                    activeContinuation = null

                    handler.post {
                        engine.stopLoading()
                        engine.loadUrl("about:blank")
                        engine.resetEnvironment(currentBaseUrl)
                    }
                }

            }

        }

}