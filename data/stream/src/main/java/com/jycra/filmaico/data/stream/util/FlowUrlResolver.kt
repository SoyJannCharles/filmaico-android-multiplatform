package com.jycra.filmaico.data.stream.util

interface FlowUrlResolver {

    suspend fun resolve(url: String, userAgent: String, headers: Map<String, String> = emptyMap()): String

}