package com.jycra.filmaico.core.network

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import java.net.URI
import javax.inject.Inject

class StreamExtractionEngine @Inject constructor(
    private val httpFetcher: HttpFetcher,
    private val jsResolver: JsResolver,
    private val evalProcessor: EvalProcessor
) {

    suspend fun extract(url: String, depth: Int = 0): String? = coroutineScope {

        ensureActive()

        if (depth > 5) return@coroutineScope null

        return@coroutineScope runCatching {

            ensureActive()

            val uri = URI(url)
            val predictedJsUrl = "${uri.scheme}://${uri.host}/main.js"

            val deferredHtml = async {
                ensureActive()
                httpFetcher.getHtml(url)
            }

            val deferredPredictedJs = async {
                ensureActive()
                httpFetcher.getJs(predictedJsUrl)
            }

            val html = deferredHtml.await()

            ensureActive()

            if (html == null) return@coroutineScope null

            evalProcessor.extractM3U8(html)?.let {
                deferredPredictedJs.cancel()
                return@coroutineScope it
            }

            val videoId = HtmlParser.extractVideoId(url)
            val scripts = HtmlParser.extractScripts(html, url)

            for (scriptUrl in scripts) {

                ensureActive()

                val jsCode = if (scriptUrl.contains("main.js")) {
                    deferredPredictedJs.await() ?: httpFetcher.getJs(scriptUrl)
                } else {
                    httpFetcher.getJs(scriptUrl)
                }

                ensureActive()

                if (jsCode == null || (!jsCode.contains("finalURL") && !jsCode.contains("destination"))) {
                    continue
                }

                val resolved = jsResolver.resolve(jsCode, url).orEmpty()

                ensureActive()

                if (resolved.isEmpty()) continue

                val rebuilt = HtmlParser.rebuildUrl(resolved, videoId)

                return@coroutineScope extract(rebuilt, depth + 1)

            }

            null

        }.getOrElse { e ->
            null
        }

    }

}