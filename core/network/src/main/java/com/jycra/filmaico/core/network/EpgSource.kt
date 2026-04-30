package com.jycra.filmaico.core.network

import com.jycra.filmaico.core.network.api.EpgApi
import com.jycra.filmaico.data.media.data.service.EpgService
import java.io.InputStream
import java.util.zip.GZIPInputStream
import javax.inject.Inject

class EpgSource @Inject constructor(
    private val epgApi: EpgApi
) : EpgService {

    override suspend fun getEpgXmlStream(): InputStream {

        val response = epgApi.getCompressedEpg()

        return if (response.isSuccessful) {

            val compressedStream = response.body()?.byteStream()
                ?: throw Exception("Error: Body nulo")

            GZIPInputStream(compressedStream)

        } else {
            throw Exception("Error de red: ${response.code()}")
        }

    }

}