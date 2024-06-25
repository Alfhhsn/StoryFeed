package com.dicoding.storyfeed.api

import com.dicoding.storyfeed.response.DetailResponse
import com.dicoding.storyfeed.response.LoginResponse
import com.dicoding.storyfeed.response.RegisterResponse
import com.dicoding.storyfeed.response.StoryResponse
import com.dicoding.storyfeed.response.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun detailStories(
        @Path("id") id: String,
    ): DetailResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): UploadStoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): StoryResponse
}