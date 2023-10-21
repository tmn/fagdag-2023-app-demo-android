package io.tmn.sanntidsappenfagdagdemoandroid.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = ""

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(HeaderInterceptor())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(httpClient)
    .build()
interface StopRegisterAPIService {

}

object StopRegisterApi {
    val geocoderService: StopRegisterAPIService by lazy { retrofit.create(StopRegisterAPIService::class.java) }
}
