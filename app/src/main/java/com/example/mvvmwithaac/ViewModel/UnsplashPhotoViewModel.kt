package com.example.mvvmwithaac.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mvvmwithaac.Repository.UnsplashRepository
import com.example.mvvmwithaac.Room.Unsplash
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto

class UnsplashPhotoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UnsplashRepository(application)
    private val unsplashes = repository.getAll()

    fun getAll(): LiveData<List<Unsplash>> {
        return this.unsplashes
    }

    fun insert(unsplash: Unsplash) {
        repository.insert(unsplash)
    }

    fun delete(unsplash: Unsplash) {
        repository.delete(unsplash)
    }
}