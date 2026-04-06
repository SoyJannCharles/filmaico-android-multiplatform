package com.jycra.filmaico.core.player

import com.jycra.filmaico.data.stream.data.cache.StreamManifestCache
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RamManifestCache @Inject constructor(): StreamManifestCache {

    private val MAX_ENTRIES = 50

    private val cache = object : LinkedHashMap<String, Pair<String, ByteArray>>(MAX_ENTRIES, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, Pair<String, ByteArray>>?): Boolean {
            return size > MAX_ENTRIES
        }
    }

    @Synchronized
    override fun put(requestedUrl: String, finalUrl: String, content: String) {
        val key = normalizeKey(requestedUrl)
        cache[key] = Pair(finalUrl, content.toByteArray(Charsets.UTF_8))
    }

    @Synchronized
    fun pop(url: String): Pair<String, ByteArray>? {
        val key = normalizeKey(url)
        return cache.remove(key)
    }

    @Synchronized
    override fun get(url: String): Pair<String, ByteArray>? {
        val key = normalizeKey(url)
        return cache[key]
    }

    @Synchronized
    override fun clear() {
        cache.clear()
    }

    private fun normalizeKey(url: String): String {
        return url.substringBefore("?")
    }

}