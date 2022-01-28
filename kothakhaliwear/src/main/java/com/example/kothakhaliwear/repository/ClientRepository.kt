package com.example.kothakhaliwear.repository

import com.example.kothakhaliwear.api.ClientAPI
import com.example.kothakhaliwear.api.MyAPIRequest
import com.example.kothakhaliwear.api.ServiceBuilder
import com.example.kothakhaliwear.response.AdResponse
import com.example.kothakhaliwear.response.RegisterLoginResponse

class ClientRepository: MyAPIRequest() {
    private val clientAPI = ServiceBuilder.buildService(ClientAPI::class.java)

    //login Client
    suspend fun checkclient(email:String, password:String): RegisterLoginResponse {
        return apiRequest {
            clientAPI.checkclient(email,password)
        }
    }

    //Get All Ad
    suspend fun getallads(): AdResponse {
        return apiRequest {
            clientAPI.getAllAds(ServiceBuilder.token!!)
        }
    }
}