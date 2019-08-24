package com.example.mvvmwithaac.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmwithaac.R
import com.example.mvvmwithaac.Room.Unsplash
import com.example.mvvmwithaac.Util.GlideApp
import kotlinx.android.synthetic.main.item_photo.view.*

class UnsplashAdapter constructor(context: Context) : RecyclerView.Adapter<UnsplashAdapter.UnsplashViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    private var mListOfPhotos: List<Unsplash> = mutableListOf()

    // 클릭리스너 interface
    interface ItemClick
    {
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnsplashViewHolder {
        return UnsplashViewHolder(mLayoutInflater.inflate(R.layout.item_photo, parent, false))
    }

    override fun onBindViewHolder(holder: UnsplashViewHolder, position: Int) {
        if(itemClick != null)
        {
            holder?.itemView?.setOnClickListener { v ->
                itemClick?.onClick(v, position)
            }
        }

        val photo = mListOfPhotos[position]

        photo.let {
            with(holder.iv_item_photo) {

                GlideApp.with(context)
                    .load(photo.un_url)
                    .fitCenter()
                    .into(holder.iv_item_photo)
            }
        }
    }

    override fun getItemCount(): Int {
        return mListOfPhotos.size
    }

    fun setListOfPhotos(listOfPhotos: List<Unsplash>?) {
        if (listOfPhotos != null) {
            mListOfPhotos = listOfPhotos
            notifyDataSetChanged()
        }
    }

    class UnsplashViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_item_photo: ImageView = view.item_photo
    }
}