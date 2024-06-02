package org.sakuram.relation.repository

import android.os.StrictMode
import kotlinx.coroutines.CoroutineScope
import retrofit2.Response

object CommonApiRepository {
    fun <T>callApiSynchronous(callDef: () -> Response<T>): T? {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                // .detectNetwork()
                .penaltyLog()
                .build()
        )

        val call: Response<T>?
        try {
            call = callDef()
        } catch(ex: Exception) {
            println("Exception: ${ex.message}")
            ex.printStackTrace()
            return null
        }
        if (call.isSuccessful) {
            println("Successful: ${call.body()?.toString()}")
            return call.body()
        } else {
            println("Failed: ${call.message()}")
            return null
        }
    }

    suspend fun <T>callApiAsynchronous(coroutineScope: CoroutineScope, callDef: suspend CoroutineScope.() -> Response<T>): T? {
        val call: Response<T>?

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                // .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
        )

        try {
            call = callDef.invoke(coroutineScope)
        } catch(ex: Exception) {
            println("Exception: ${ex.message}")
            ex.printStackTrace()
            return null
        }
        if (call.isSuccessful) {
            println("Successful: ${call.body()?.toString()}")
            return call.body()
        } else {
            println("Failed: ${call.message()}")
            return null
        }
    }

}