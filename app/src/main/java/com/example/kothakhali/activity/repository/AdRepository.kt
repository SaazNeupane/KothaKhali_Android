package com.example.kothakhali.activity.repository

import android.content.Context
import com.example.kothakhali.activity.api.AdAPI
import com.example.kothakhali.activity.api.MyAPIRequest
import com.example.kothakhali.activity.api.ServiceBuilder
import com.example.kothakhali.activity.db.KothaKhaliDB
import com.example.kothakhali.activity.model.AdList
import com.example.kothakhali.activity.model.Comment
import com.example.kothakhali.activity.model.Wishlist
import com.example.kothakhali.activity.response.*
import okhttp3.MultipartBody

class AdRepository:MyAPIRequest() {
    private val adAPI= ServiceBuilder.buildService(AdAPI::class.java)

    //Add Ad

    suspend fun addad(adList: AdList):AdAddResponse{
        return apiRequest {
            adAPI.addAd(
                    ServiceBuilder.token!!,adList
            )
        }
    }

    //Upload Photo
    suspend fun uploadImage(id: String, body: MultipartBody.Part)
            : ImageResponse {
        return apiRequest {
            adAPI.uploadImage(ServiceBuilder.token!!, id, body)
        }
    }

    //Delete Ad
    suspend fun deletead(id: String): DeleteAdResponse {
        return apiRequest {
            adAPI.deletead(ServiceBuilder.token!!,id)
        }
    }


    //For roomDatabase
    suspend fun insertBulkAds(context : Context, adList : List<AdList>){
        // Delete all ads
        KothaKhaliDB.getInstance(context).getAdDao().DeleteAllAds()
        // Insert all data in database
        KothaKhaliDB.getInstance(context).getAdDao().insertBulkAds(adList)
    }


    //Get All Ad
    suspend fun getallads(): AllAdResponse {
        return apiRequest {
            adAPI.getAllAds(ServiceBuilder.token!!)
        }
    }

    //Get All Ad
    suspend fun getad(id: String): SingleAdResponse {
        return apiRequest {
            adAPI.getad(ServiceBuilder.token!!,id)
        }
    }

    //Get My Ad
    suspend fun getmyads(): AllAdResponse {
        return apiRequest {
            adAPI.getmyAds(ServiceBuilder.token!!)
        }
    }

    //Get My Wishlist
    suspend fun getmywishlist(): WishlistResponse {
        return apiRequest {
            adAPI.getmywishlist(ServiceBuilder.token!!)
        }
    }

    //Add Wishlist
    suspend fun wishlistadd(wishlist: Wishlist):WishlistAddResponse{
        return apiRequest {
            adAPI.wishlistadd(
                    ServiceBuilder.token!!,wishlist
            )
        }
    }
    //Delete Wishlist
    suspend fun deletewishlist(id: String): DeleteAdResponse {
        return apiRequest {
            adAPI.deletewishlist(ServiceBuilder.token!!,id)
        }
    }

    //Add Comment
    suspend fun commentadd(comment: Comment):CommentAddResponse{
        return apiRequest {
            adAPI.commentadd(
                    ServiceBuilder.token!!,comment
            )
        }
    }

    //Get Comment
    suspend fun getcomment(id: String): CommentResponse {
        return apiRequest {
            adAPI.getcomment(id)
        }
    }
}