package org.sakuram.relation.repository

import kotlinx.coroutines.CoroutineScope
import okhttp3.MediaType
import okhttp3.RequestBody
import org.sakuram.relation.api.RestAPI
import org.sakuram.relation.apimodel.RetrievePersonAttributesResponseVO
import retrofit2.Response
import kotlin.coroutines.coroutineContext

object PersonRelationApiRepository {
    suspend fun retrievePersonAttributes(entityId: Long): RetrievePersonAttributesResponseVO? {
        val lambda : suspend CoroutineScope.() -> Response<RetrievePersonAttributesResponseVO> = {
            RestAPI.uravugalPersonRelationApi.retrievePersonAttributes(
                RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    // JSONObject(mapOf("entityId" to entityId)).toString()
                    entityId.toString()
                )
            )
        }
        return CommonApiRepository.callApiAsynchronous<RetrievePersonAttributesResponseVO>(CoroutineScope(
            coroutineContext), lambda)
    }
}