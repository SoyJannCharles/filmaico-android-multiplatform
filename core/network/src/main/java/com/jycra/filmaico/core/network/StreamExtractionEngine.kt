package com.jycra.filmaico.core.network

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
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

            val deferredHtml = async {
                ensureActive()
                httpFetcher.getHtml(url)
            }

            val html = deferredHtml.await()

            ensureActive()

            if (html == null) return@coroutineScope null

            evalProcessor.extractM3U8(html)?.let {
                return@coroutineScope it
            }

            val scripts = HtmlParser.extractScripts(html, url)

            for (scriptUrl in scripts) {

                ensureActive()

                val jsCode = httpFetcher.getJs(scriptUrl)

                ensureActive()

                if (jsCode == null || (!jsCode.contains("finalURL") && !jsCode.contains("destination"))) {
                    continue
                }

                val resolved = jsResolver.resolve(jsCode, url).orEmpty()

                ensureActive()

                if (resolved.isEmpty()) continue

                val rebuilt = HtmlParser.resolveTargetUrl(resolved, url)

                return@coroutineScope extract(rebuilt, depth + 1)

            }

            null

        }.getOrElse { e ->
            null
        }

    }

}