package com.example.storyappdicoding.api

import com.example.storyappdicoding.response.GetStoriesResponse
import com.example.storyappdicoding.response.LoginResponse
import com.example.storyappdicoding.response.RegisterAddStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call <RegisterAddStoryResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call <LoginResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file : MultipartBody.Part
    ): Call <RegisterAddStoryResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String,
    ): Call <GetStoriesResponse>

}