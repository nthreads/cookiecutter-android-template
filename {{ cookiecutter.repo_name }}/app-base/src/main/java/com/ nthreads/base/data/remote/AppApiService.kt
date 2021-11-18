package com.nthreads.base.data.remote

import com.nthreads.base.data.responses.BasicResponse
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AppApiService {

    @GET("appVersion")
    fun getVersion(
        @Query("app") app: String,
        @Query("appPlatform") appPlatform: String = "android"
    ): Single<BasicResponse<JSONObject>>

    @GET("auth/get-nonce")
    fun getNonce(): Single<BasicResponse<String>>

    @POST("users/devices")
    fun registerDevice(
        @Query("userId") userId: String,
        @Body params: HashMap<String, String>
    ): Single<BasicResponse<JSONObject>>

}