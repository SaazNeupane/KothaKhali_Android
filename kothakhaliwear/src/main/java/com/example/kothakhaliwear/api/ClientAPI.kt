package com.example.kothakhaliwear.api

import com.example.kothakhaliwear.response.AdResponse
import com.example.kothakhaliwear.response.RegisterLoginResponse
import retrofit2.Response
import retrofit2.http.*

interface ClientAPI {
    //Login Client
    @FormUrlEncoded
    @POST("client/login")
    suspend fun checkclient(
            @Field("email") email: String,
            @Field("password") password: String
    ):Response<RegisterLoginResponse>

    //Get All Ads
    @GET("ad/fetch")
    suspend fun getAllAds(
            @Header("Authorization") token : String
    ):Response<AdResponse>
}