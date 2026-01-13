package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.stream.model.DrmKeys
import com.jycra.filmaico.domain.stream.repository.AttrStreamRepository
import javax.inject.Inject

class GetDrmKeyUseCase @Inject constructor(
    private val attrStreamRepository: AttrStreamRepository
) {

    suspend operator fun invoke(
        contentId: String,
        licenseUrl: String,
        forceRefresh: Boolean = false,
        onReportStatus: (String) -> Unit = {}
    ): DrmKeys? {

        if (forceRefresh) {
            onReportStatus("Forzando actualización desde servidor...")
            return fetchAndCacheNewKey(contentId, licenseUrl)
        }

        onReportStatus("Buscando llaves en caché local...")
        val localKey = attrStreamRepository.getCachedDrmKey(contentId)
        if (localKey != null) {
            return localKey
        }

        onReportStatus("Consultando base de datos remota...")
        val remoteKey = attrStreamRepository.getDrmKeyFromRemote(contentId)
        if (remoteKey != null) {
            // Si la encontramos en Firebase, la guardamos localmente para la próxima vez
            attrStreamRepository.saveDrmKeyToCache(contentId, remoteKey)
            return remoteKey
        }

        // 3. ÚLTIMO RECURSO: IR A LA RED
        onReportStatus("Solicitando licencias al proveedor...")
        return fetchAndCacheNewKey(contentId, licenseUrl)

    }

    private suspend fun fetchAndCacheNewKey(channelId: String, licenseUrl: String): DrmKeys? {
        val newKey = attrStreamRepository.fetchDrmKeysFromNetwork(licenseUrl)
        // Guardamos la nueva clave en ambos cachés
        attrStreamRepository.saveDrmKeyToCache(channelId, newKey)
        attrStreamRepository.saveDrmKeyToRemote(channelId, newKey)
        return newKey
    }

}