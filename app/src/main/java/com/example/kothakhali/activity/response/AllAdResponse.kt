package com.example.kothakhali.activity.response

import com.example.kothakhali.activity.model.AdList

class AllAdResponse (
    val success : Boolean?=null,
    val data : MutableList<AdList>? = null
)