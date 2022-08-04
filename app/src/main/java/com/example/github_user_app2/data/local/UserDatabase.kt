package com.example.github_user_app2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.internal.synchronized


@Database(
    entities = [FavoriteUser::class],
    version = 1
)

abstract class UserDatabase : RoomDatabase(){
    companion object{
        var INSTANCE : UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase?{
            if (INSTANCE==null){
                kotlin.synchronized(UserDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, UserDatabase::class.java,"user_database").build()
                }
            }
            return INSTANCE
        }

    }

    abstract fun favoriteUserDao(): FavoriteUserDao
}