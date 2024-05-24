package org.sakuram.relation.api

import org.sakuram.relation.Constants
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
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

object RestAPI {

    val uravugalProjectUserApi: UravugalProjectUserApi

    init {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(JsonDateAdapter())
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
    }

}

object RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        println("Outgoing request to ${request.url()}")
        return chain.proceed(request)
    }
}

class JsonDateAdapter : JsonAdapter<Date>() {
    private val dateFormat = SimpleDateFormat(Constants.SERVER_FORMAT, Locale.getDefault())

    @FromJson
    override fun fromJson(reader: JsonReader): Date? {
        return try {
            val dateAsString = reader.nextString()
            println(dateAsString)
            dateFormat.parse(dateAsString)?.let { Date(it.time) }
        } catch (e: Exception) {
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