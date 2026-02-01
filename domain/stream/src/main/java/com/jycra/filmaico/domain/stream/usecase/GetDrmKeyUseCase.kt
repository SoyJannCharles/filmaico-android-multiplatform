package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.media.model.stream.DrmInfo
import com.jycra.filmaico.domain.media.model.stream.DrmKeys
import com.jycra.filmaico.domain.media.model.stream.Key
import com.jycra.filmaico.domain.stream.repository.AttrStreamRepository
import javax.inject.Inject

class GetDrmKeyUseCase @Inject constructor(
    private val attrStreamRepository: AttrStreamRepository
) {

    suspend operator fun invoke(
        contentId: String,
        drmInfo: DrmInfo,
        forceRefresh: Boolean = false,
        onStatusUpdate: (String) -> Unit = {}
    ): DrmKeys? {

        if (!forceRefresh && drmInfo.staticKeys.isValid()) {
            onStatusUpdate("Validando llaves de seguridad...")
            return DrmKeys(
                listOf(
                    Key(
                        kty = "oct",
                        k = drmInfo.staticKeys.k,
                        kid = drmInfo.staticKeys.kid,
                    )
                )
            )
        }

        if (drmInfo.licenseUrl.isNotBlank()) {
            onStatusUpdate("Obteniendo autorización de red...")
            return attrStreamRepository.fetchDrmKeysFromNetwork(drmInfo.licenseUrl)
        }

        return null

    }

}