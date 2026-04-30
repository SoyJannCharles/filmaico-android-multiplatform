package com.jycra.filmaico.data.media.data.service

import java.io.InputStream

interface EpgService {

    suspend fun getEpgXmlStream(): InputStream

}