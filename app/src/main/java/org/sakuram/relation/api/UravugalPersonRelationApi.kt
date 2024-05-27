package org.sakuram.relation.api

import okhttp3.RequestBody
import org.sakuram.relation.apimodel.RetrievePersonAttributesResponseVO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UravugalPersonRelationApi {
    @POST("/relation/basic/retrievePersonAttributes")
    suspend fun retrievePersonAttributes(@Body requestBody: RequestBody): Response<RetrievePersonAttributesResponseVO>
}
