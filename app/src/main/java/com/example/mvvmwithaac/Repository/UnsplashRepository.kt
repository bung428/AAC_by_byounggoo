package com.example.mvvmwithaac.Repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.mvvmwithaac.Room.*

class UnsplashRepository(application: Application) {

    private val unsplashDatabase = UnsplashDatabase.getInstance(application)!!
    private val unsplashDao: UnsplashDao = unsplashDatabase.unsplashDao()
    private val unsplashes: LiveData<List<Unsplash>> = unsplashDao.getAll()

    fun getAll(): LiveData<List<Unsplash>> {
        return unsplashes
    }

    fun insert(unsplash: Unsplash) {
        try {
            val thread = Thread(Runnable {
                unsplashDao.insert(unsplash) })
            thread.start()
        } catch (e: Exception) { }
    }

    fun delete(unsplash: Unsplash) {
        try {
            val thread = Thread(Runnable {
                unsplashDao.delete(unsplash)
            })
            thread.start()
        } catch (e: Exception) { }
    }

}