package com.dicoding.picodiploma.loginwithanimation.data.api

import com.dicoding.picodiploma.loginwithanimation.response.DetailResponse
import com.dicoding.picodiploma.loginwithanimation.response.LogInResponse
import com.dicoding.picodiploma.loginwithanimation.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.response.StoryResponse
import com.dicoding.picodiploma.loginwithanimation.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse


    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LogInResponse

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20,
        @Query("location") location: Int = 0
    ) : StoryResponse

    @GET("stories/{id}")
    suspend fun detailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ) : DetailResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): UploadResponse
}
