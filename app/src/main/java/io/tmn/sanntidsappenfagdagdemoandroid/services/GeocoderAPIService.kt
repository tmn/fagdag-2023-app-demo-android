package io.tmn.sanntidsappenfagdagdemoandroid.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.tmn.sanntidsappenfagdagdemoandroid.models.Geocoding
import okhttp3.Dns
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.Inet4Address
import java.net.InetAddress

private const val BASE_URL = "https://api.entur.io/geocoder/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request: Request = original.newBuilder()
            .header("ET-Client-Name", "sanntidsappen-fagdag-demo-dev")
            .method(original.method, original.body)
            .build()
        return chain.proceed(request)
    }
}

class DnsSelector() : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        return Dns.SYSTEM.lookup(hostname).filter { Inet4Address::class.java.isInstance(it) }
    }
}

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(HeaderInterceptor())
    .dns(DnsSelector())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(httpClient)
    .build()

interface GeocoderAPIService {
    @GET("autocomplete")
    suspend fun getAutocompleteBusStop(@Query("text") query: String, @Query("layers") layers: String = "venue") : Geocoding

    @GET("reverse")
    suspend fun getNearbyStops(@Query("point.lat") lat: Double, @Query("point.lon") lon: Double, @Query("size") size: Int = 5, @Query("layers") layers: String = "venue")
}

object GeocoderApi {
    val geocoderService: GeocoderAPIService by lazy {
        retrofit.create(GeocoderAPIService::class.java)
    }
}
