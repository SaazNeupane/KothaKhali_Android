package com.example.kothakhali.activity.api

import com.example.kothakhali.activity.model.Client
import com.example.kothakhali.activity.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ClientAPI {
    //Register Client
    @POST("client/register")
    suspend fun clientRegister(
        @Body client: Client
    ): Response<RegisterLoginResponse>

    //Login Client
    @FormUrlEncoded
    @POST("client/login")
    suspend fun checkclient(
            @Field("email") email: String,
            @Field("password") password: String
    ):Response<RegisterLoginResponse>

    //Update Client Details
    @PUT("/client/update")
    suspend fun update(
        @Header("Authorization") token: String,
        @Body client: Client
    ): Response<UpdateResponse>

    //Client
    @GET("/profile")
    suspend fun profile(
            @Header("Authorization") token : String
    ):Response<CurrentClientResponse>

    //Client Single
    @GET("client/fetch/single/{id}")
    suspend fun getclient(
            @Header("Authorization") token : String,
            @Path ("id") id: String
    ):Response<SingleClientResponse>


}