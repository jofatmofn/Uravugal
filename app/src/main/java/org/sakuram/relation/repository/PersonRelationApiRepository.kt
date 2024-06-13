package org.sakuram.relation.repository

import com.squareup.moshi.adapter
import kotlinx.coroutines.CoroutineScope
import okhttp3.MediaType
import okhttp3.RequestBody
import org.sakuram.relation.api.RestAPI
import org.sakuram.relation.apimodel.GraphVO
import org.sakuram.relation.apimodel.RetrievePersonAttributesResponseVO
import org.sakuram.relation.apimodel.RetrieveRelationAttributesResponseVO
import org.sakuram.relation.apimodel.RetrieveRelationsRequestVO
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

    suspend fun retrieveRelationAttributes(entityId: Long): RetrieveRelationAttributesResponseVO? {
        val lambda : suspend CoroutineScope.() -> Response<RetrieveRelationAttributesResponseVO> = {
            RestAPI.uravugalPersonRelationApi.retrieveRelationAttributes(
                RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    entityId.toString()
                )
            )
        }
        return CommonApiRepository.callApiAsynchronous<RetrieveRelationAttributesResponseVO>(CoroutineScope(
            coroutineContext), lambda)
    }

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun retrieveRelations(retrieveRelationsRequestVO: RetrieveRelationsRequestVO): GraphVO? {
        val lambda : suspend CoroutineScope.() -> Response<GraphVO> = {
            RestAPI.uravugalPersonRelationApi.retrieveRelations(
                RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    RestAPI.moshi.adapter<RetrieveRelationsRequestVO>().toJson(retrieveRelationsRequestVO).toString()
                )
            )
        }
        return CommonApiRepository.callApiAsynchronous<GraphVO>(CoroutineScope(
            coroutineContext), lambda)
    }

}