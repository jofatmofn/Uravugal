package org.sakuram.relation.repository

import com.squareup.moshi.adapter
import kotlinx.coroutines.CoroutineScope
import okhttp3.MediaType
import okhttp3.RequestBody
import org.sakuram.relation.api.RestAPI
import org.sakuram.relation.apimodel.GraphVO
import org.sakuram.relation.apimodel.RelatedPersonsVO
import retrofit2.Response
import kotlin.coroutines.coroutineContext

object AlgoApiRepository {
    @OptIn(ExperimentalStdlibApi::class)
    suspend fun retrieveRelationPath(relatedPersonsVO: RelatedPersonsVO): GraphVO? {
        val lambda : suspend CoroutineScope.() -> Response<GraphVO> = {
            RestAPI.uravugalAlgoApi.retrieveRelationPath(
                RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    RestAPI.moshi.adapter<RelatedPersonsVO>().toJson(relatedPersonsVO).toString()
                )
            )
        }
        return CommonApiRepository.callApiAsynchronous<GraphVO>(
            CoroutineScope(
            coroutineContext
            ), lambda)
    }
}