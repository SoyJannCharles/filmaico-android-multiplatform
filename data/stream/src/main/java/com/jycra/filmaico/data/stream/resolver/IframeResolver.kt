package com.jycra.filmaico.data.stream.resolver

import kotlinx.coroutines.flow.Flow

interface IframeResolver {

    fun getLastResolvedDomain(): String?

    suspend fun resolveStatic(iframeUrl: String, preloadedHtml: String? = null): String?
    fun resolveWithWebView(iframeUrl: String): Flow<String>

}