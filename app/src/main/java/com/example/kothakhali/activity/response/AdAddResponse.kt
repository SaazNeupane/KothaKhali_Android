package com.example.kothakhali.activity.response

import com.example.kothakhali.activity.model.AdList

data class AdAddResponse(
        val success: Boolean?=null,
        val message: String?=null,
        val data: AdList?= null
)