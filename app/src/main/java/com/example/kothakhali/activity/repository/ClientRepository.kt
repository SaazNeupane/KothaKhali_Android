package com.example.kothakhali.activity.repository

import com.example.kothakhali.activity.api.ClientAPI
import com.example.kothakhali.activity.api.MyAPIRequest
import com.example.kothakhali.activity.api.ServiceBuilder
import com.example.kothakhali.activity.model.AdList
import com.example.kothakhali.activity.model.Client
import com.example.kothakhali.activity.response.*

class ClientRepository:MyAPIRequest() {
    private val clientAPI = ServiceBuilder.buildService(ClientAPI::class.java)

    //register Client
    suspend fun clientRegister(client: Client):RegisterLoginResponse{
        return apiRequest {
            clientAPI.clientRegister(client)
        }
    }

    //login Client
    suspend fun checkclient(email:String, password:String):RegisterLoginResponse{
        return apiRequest {
            clientAPI.checkclient(email,password)
        }
    }

    //Update
    suspend fun update(client: Client): UpdateResponse {
        return apiRequest {
            clientAPI.update(
                ServiceBuilder.token!!,client
            )
        }
    }

    //Current Client
    suspend fun profile():CurrentClientResponse{
        return apiRequest {
            clientAPI.profile(ServiceBuilder.token!!)
        }
    }

    //Single Client
    suspend fun getclient(id: String): SingleClientResponse {
        return apiRequest {
            clientAPI.getclient(ServiceBuilder.token!!,id)
        }
    }

}