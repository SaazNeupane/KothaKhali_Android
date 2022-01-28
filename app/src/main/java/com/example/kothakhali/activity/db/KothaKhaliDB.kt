package com.example.kothakhali.activity.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kothakhali.activity.dao.AdDAO
import com.example.kothakhali.activity.model.AdList


@Database(
        entities = [(AdList::class)],
        version = 1,
        exportSchema = false
)

abstract  class KothaKhaliDB: RoomDatabase() {
    abstract fun getAdDao() : AdDAO
    companion object{
        @Volatile
        private var instance : KothaKhaliDB? = null
        fun getInstance(context: Context): KothaKhaliDB{
            if(instance == null){
                synchronized(KothaKhaliDB::class){
                    instance = buildDatabase(context)
                }
            }
            return instance!!
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                KothaKhaliDB::class.java,
                "KothaKhaliDB"
            ).build()
    }
}