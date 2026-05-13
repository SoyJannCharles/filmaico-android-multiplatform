package com.jycra.filmaico.core.network.api

import com.jycra.filmaico.core.network.util.response.EdgePoolResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface EdgeNodeApi {

    @GET
    suspend fun getEdgeNodes(
        @Url url: String
    ): Response<EdgePoolResponse>

}