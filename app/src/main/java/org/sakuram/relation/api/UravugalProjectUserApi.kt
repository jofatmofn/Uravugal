package org.sakuram.relation.api

import okhttp3.RequestBody
import org.sakuram.relation.apimodel.ProjectVO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UravugalProjectUserApi {
    @POST("/relation/projectuser/switchProject")
    fun switchProject(@Body requestBody: RequestBody): Call<ProjectVO>

}