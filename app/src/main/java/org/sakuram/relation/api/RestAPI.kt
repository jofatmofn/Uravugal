package org.sakuram.relation.api

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.sakuram.relation.utility.Constants
import org.sakuram.relation.utility.UravugalPreferences
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

object RestAPI {

    val uravugalProjectUserApi: UravugalProjectUserApi
    val uravugalPersonRelationApi: UravugalPersonRelationApi

    init {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(JsonDateAdapter())
            .add(JsonByteArrayAdapter())
            .build()
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(RequestInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.URAVUGAL_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        uravugalProjectUserApi = retrofit
            .create(UravugalProjectUserApi::class.java)
        uravugalPersonRelationApi = retrofit
            .create(UravugalPersonRelationApi::class.java)
    }

}

object RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var jSessionId: String? = UravugalPreferences.jSessionId

        if (!request.url().toString().contains(Constants.URAVUGAL_SWITCH_PROJECT_URL) &&
            !request.url().toString().contains(Constants.URAVUGAL_SWITCH_LANGUAGE_URL) &&
            !jSessionId.equals("")) {
            request = request.newBuilder().addHeader("Cookie", "JSESSIONID=$jSessionId").build()
        }
        println("Outgoing request to ${request.url()} with SessionId $jSessionId")
        val response: Response = chain.proceed(request)
        if (response.request().url().toString().contains(Constants.URAVUGAL_SWITCH_PROJECT_URL) ||
            response.request().url().toString().contains(Constants.URAVUGAL_SWITCH_LANGUAGE_URL)) {
            val cookieHeaders = response.headers("Set-Cookie")
            for (cookieHeader in cookieHeaders) {
                if (cookieHeader.contains("JSESSIONID=")) {
                    jSessionId = cookieHeader.substring(11, cookieHeader.indexOf(';'))
                    UravugalPreferences.jSessionId = jSessionId
                    println("SessionId from API: $jSessionId")
                }
            }
        }
        return response
    }

}

class JsonDateAdapter : JsonAdapter<Date>() {
    private val dateFormat = SimpleDateFormat(Constants.SERVER_FORMAT, Locale.getDefault())

    @FromJson
    override fun fromJson(reader: JsonReader): Date? {
        return try {
            val dateAsString = if (reader.peek() == JsonReader.Token.NULL)
                reader.nextNull() else reader.nextString()

            if (dateAsString != null) {
                dateFormat.parse(dateAsString)?.let { Date(it.time) }
            } else {
                null
            }
        } catch (e: Exception) {
            println("JsonDateAdapter Stack Trace Start")
            e.printStackTrace()
            println("JsonDateAdapter Stack Trace End")
            null
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Date?) {
        if (value != null) {
            writer.value(value.toString())
        }
    }
}

class JsonByteArrayAdapter : JsonAdapter<ByteArray>() {

    @FromJson
    override fun fromJson(reader: JsonReader): ByteArray? {
        return try {
            (if (reader.peek() == JsonReader.Token.NULL)
                reader.nextNull() else reader.nextString())?.toByteArray()
        } catch (e: Exception) {
            println("JsonByteArrayAdapter Stack Trace Start")
            e.printStackTrace()
            println("JsonByteArrayAdapter Stack Trace End")
            null
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: ByteArray?) {
        if (value != null) {
            writer.value(value.toString())
        }
    }
}