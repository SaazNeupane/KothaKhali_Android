package com.example.kothakhali.activity.model

import androidx.room.Entity


@Entity
data class Client(
    val _id: String? = null,
    val name: String? = null,
    val mobile: String? = null,
    val email: String? = null,
    val password: String? = null,
    val image:String? =null
)