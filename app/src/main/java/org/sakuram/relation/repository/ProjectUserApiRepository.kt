package org.sakuram.relation.repository

import android.os.StrictMode
import okhttp3.RequestBody
import org.sakuram.relation.api.RestAPI
import org.sakuram.relation.apimodel.ProjectVO
import retrofit2.Response

object ProjectUserApiRepository {
    fun switchProject(projectId: String): ProjectVO? {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                // .detectNetwork()
                .penaltyLog()
                .build()
        )

        var call: Response<ProjectVO>?
        try {
            call = RestAPI.uravugalProjectUserApi.switchProject(
                RequestBody.create(
                    okhttp3.MediaType.parse("text/plain; charset=utf-8"),
                    // JSONObject(mapOf("entityId" to entityId)).toString()
                    projectId
                )
            )
                .execute()
        } catch(ex: Exception) {
            println("Exception: ${ex.message}")
            ex.printStackTrace()
            return null
        }
        if (call != null) {
            if (call.isSuccessful) {
                println("switchProject Successful: ${call.body()?.toString()}")
                return call.body()
            } else {
                println("switchProject Failed: ${call.message()}")
                return null
            }
        } else {
            println("Unknown state trying to call switchProject}")
            return null
        }
    }

    suspend fun xxx(projectId: String) =  RestAPI.uravugalProjectUserApi.xxx(
        RequestBody.create(
            okhttp3.MediaType.parse("text/plain; charset=utf-8"),
            // JSONObject(mapOf("entityId" to entityId)).toString()
            projectId
        )
    )
}