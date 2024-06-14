package org.sakuram.relation.api

import okhttp3.RequestBody
import org.sakuram.relation.apimodel.GraphVO
import org.sakuram.relation.apimodel.RetrieveAppStartValuesResponseVO
import org.sakuram.relation.apimodel.RetrievePersonAttributesResponseVO
import org.sakuram.relation.apimodel.RetrieveRelationAttributesResponseVO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UravugalPersonRelationApi {
    @POST("/relation/basic/retrievePersonAttributes")
    suspend fun retrievePersonAttributes(@Body requestBody: RequestBody): Response<RetrievePersonAttributesResponseVO>

    @POST("/relation/basic/retrieveRelationAttributes")
    suspend fun retrieveRelationAttributes(@Body requestBody: RequestBody): Response<RetrieveRelationAttributesResponseVO>

    @POST("/relation/basic/retrieveRelations")
    suspend fun retrieveRelations(@Body requestBody: RequestBody): Response<GraphVO>

    @POST("/relation/basic/retrieveAppStartValues")
    fun retrieveAppStartValues(): Call<RetrieveAppStartValuesResponseVO>
}
