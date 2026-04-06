package com.jycra.filmaico.core.player

import android.net.Uri
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.TransferListener
import androidx.core.net.toUri

@UnstableApi
class RamCacheDataSource(
    private val upstream: DataSource,
    private val cache: RamManifestCache
) : DataSource {

    private var cachedData: ByteArray? = null
    private var bytesRead = 0

    private var currentUri: Uri? = null
    private var finalRedirectUri: Uri? = null

    override fun getUri(): Uri? {
        return if (cachedData != null) {
            finalRedirectUri
        } else {
            upstream.uri
        }
    }

    override fun getResponseHeaders(): Map<String, List<String>> {
        return if (cachedData != null) {
            emptyMap()
        } else {
            upstream.responseHeaders
        }
    }

    override fun open(dataSpec: DataSpec): Long {

        currentUri = dataSpec.uri
        val urlString = currentUri.toString()

        val dataInRam = cache.pop(urlString)

        return if (dataInRam != null) {

            finalRedirectUri = dataInRam.first.toUri()

            cachedData = dataInRam.second
            bytesRead = 0

            dataInRam.second.size.toLong()

        } else {
            finalRedirectUri = null
            upstream.open(dataSpec)
        }

    }

    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {

        val data = cachedData

        return if (data != null) {

            if (bytesRead >= data.size) return C.RESULT_END_OF_INPUT

            val bytesToRead = minOf(length, data.size - bytesRead)

            System.arraycopy(data, bytesRead, buffer, offset, bytesToRead)

            bytesRead += bytesToRead
            bytesToRead

        } else {
            upstream.read(buffer, offset, length)
        }

    }

    override fun close() {
        cachedData = null
        finalRedirectUri = null
        upstream.close()
    }

    override fun addTransferListener(transferListener: TransferListener) {
        upstream.addTransferListener(transferListener)
    }

}