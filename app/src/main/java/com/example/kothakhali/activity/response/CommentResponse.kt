package com.example.kothakhali.activity.response

import com.example.kothakhali.activity.model.Comment

class CommentResponse (
        val success : Boolean?=true,
        val message: String? = null,
        val data : ArrayList<Comment>? = null
)