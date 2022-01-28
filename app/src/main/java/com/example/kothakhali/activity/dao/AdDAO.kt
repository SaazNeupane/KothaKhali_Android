package com.example.kothakhali.activity.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kothakhali.activity.model.AdList

@Dao
interface AdDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBulkAds(adLists: List<AdList>)

    @Query("select * from AdList")
    suspend fun getallAds(): MutableList<AdList>

    @Query("DELETE FROM AdList")
    suspend fun DeleteAllAds()
}