package com.example.mvvmwithaac.Room

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface UnsplashDao {

    @Query("SELECT * FROM unsplash ORDER BY un_url ASC")
    fun getAll(): LiveData<List<Unsplash>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(unsplash: Unsplash)

    @Delete
    fun delete(unsplash: Unsplash)

}