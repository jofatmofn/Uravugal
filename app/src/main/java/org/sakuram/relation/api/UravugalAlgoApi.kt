package org.sakuram.relation.api

import okhttp3.RequestBody
import org.sakuram.relation.apimodel.GraphVO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UravugalAlgoApi {
    @POST("/relation/algo/retrieveRelationPath")
    suspend fun retrieveRelationPath(@Body requestBody: RequestBody): Response<GraphVO>

}