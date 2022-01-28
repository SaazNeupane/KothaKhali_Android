package com.example.kothakhali.activity.model

import androidx.room.Entity

@Entity
data class Comment(
        val _id: String? = null,
        val clientid: String? = null,
        val adid: String? = null,
        val comment: String? = null,
        val commentedAt: String? = null
)