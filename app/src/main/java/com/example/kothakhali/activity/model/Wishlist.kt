package com.example.kothakhali.activity.model

import androidx.room.Entity

@Entity
data class Wishlist(
        val _id: String? = null,
        val adid: String? = null,
        val clientid: String? = null,
        val WishlistedAt: String? = null
)