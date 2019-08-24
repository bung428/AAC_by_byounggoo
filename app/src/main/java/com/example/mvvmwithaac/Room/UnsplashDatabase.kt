package com.example.mvvmwithaac.Room


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Unsplash::class], version = 1)
abstract class UnsplashDatabase: RoomDatabase() {

    abstract fun unsplashDao(): UnsplashDao

    companion object {
        private var INSTANCE: UnsplashDatabase? = null

        fun getInstance(context: Context): UnsplashDatabase? {
            if (INSTANCE == null) {
                synchronized(UnsplashDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UnsplashDatabase::class.java, "unsplash")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }

}