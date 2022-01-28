package com.example.kothakhali.activity.response

import com.example.kothakhali.activity.model.Wishlist

class WishlistResponse (
        val success : Boolean?=true,
        val message: String? = null,
        val data : ArrayList<Wishlist>? = null
)