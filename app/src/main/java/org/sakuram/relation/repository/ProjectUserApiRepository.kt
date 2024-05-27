package org.sakuram.relation.repository

import okhttp3.RequestBody
import org.sakuram.relation.api.RestAPI
import org.sakuram.relation.apimodel.ProjectVO

object ProjectUserApiRepository {
    fun switchProject(projectId: String): ProjectVO? {
        return CommonApiRepository.callApiSynchronous<ProjectVO> {
            RestAPI.uravugalProjectUserApi.switchProject(
                RequestBody.create(
                    okhttp3.MediaType.parse("text/plain; charset=utf-8"),
                    // JSONObject(mapOf("entityId" to entityId)).toString()
                    projectId
                )
            )
                .execute()
        }
    }
}