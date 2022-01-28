package com.example.kothakhali.activity.api

import com.example.kothakhali.activity.model.AdList
import com.example.kothakhali.activity.model.Client
import com.example.kothakhali.activity.model.Comment
import com.example.kothakhali.activity.model.Wishlist
import com.example.kothakhali.activity.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AdAPI {
    //Add Ad
    @POST("ad/androidinsert")
    suspend fun addAd(
        @Header("Authorization") token : String,
        @Body adList : AdList
    ): Response<AdAddResponse>

    //Image Change
    @Multipart
    @PUT("/ad/insert/{id}")
    suspend fun uploadImage(
            @Header("Authorization") token: String,
            @Path ("id") id:String,
            @Part file: MultipartBody.Part
    ): Response<ImageResponse>

    //Delete Ads
    @DELETE("ad/delete/{id}")
    suspend fun deletead(
        @Header("Authorization") token: String,
        @Path ("id") id: String
    ): Response<DeleteAdResponse>

    //Get All Ads
    @GET("ad/fetch")
    suspend fun getAllAds(
        @Header("Authorization") token : String
    ):Response<AllAdResponse>

    //Get Single Ad
    @GET("ad/fetch/single/{id}")
    suspend fun getad(
        @Header("Authorization") token : String,
        @Path ("id") id: String
    ):Response<SingleAdResponse>

    //Get Client Ads
    @GET("myad")
    suspend fun getmyAds(
            @Header("Authorization") token : String
    ):Response<AllAdResponse>

    //Get Wishlist Of Client
    @GET("wishlist")
    suspend fun getmywishlist(
            @Header("Authorization") token : String
    ):Response<WishlistResponse>

    //Add wishlist
    @POST("wishlist/add")
    suspend fun wishlistadd(
            @Header("Authorization") token : String,
            @Body wishlist: Wishlist
    ): Response<WishlistAddResponse>

    //Delete Ads
    @DELETE("wishlist/delete/{id}")
    suspend fun deletewishlist(
        @Header("Authorization") token: String,
        @Path ("id") id: String
    ): Response<DeleteAdResponse>

    //Add comment
    @POST("comment/add")
    suspend fun commentadd(
            @Header("Authorization") token : String,
            @Body comment: Comment
    ): Response<CommentAddResponse>

    //Get Ad Comment
    @GET("comment/fetch/{id}")
    suspend fun getcomment(
            @Path ("id") id: String
    ):Response<CommentResponse>
}