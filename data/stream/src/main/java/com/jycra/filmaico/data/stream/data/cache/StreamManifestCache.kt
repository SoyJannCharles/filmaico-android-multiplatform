package com.jycra.filmaico.data.stream.data.cache

interface StreamManifestCache {

    fun put(requestedUrl: String, finalUrl: String, content: String)

    fun get(url: String): Pair<String, ByteArray>?

    fun clear()

}