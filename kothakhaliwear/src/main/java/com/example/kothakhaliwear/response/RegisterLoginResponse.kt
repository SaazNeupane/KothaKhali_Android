package com.example.kothakhaliwear.response

data class RegisterLoginResponse(
    val success:Boolean? = null,
    val message:String?,
    val token:String? = null
)