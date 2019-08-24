package com.example.mvvmwithaac.View

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mvvmwithaac.Adapter.UnsplashAdapter
import com.example.mvvmwithaac.R
import com.example.mvvmwithaac.Room.Unsplash
import com.example.mvvmwithaac.ViewModel.UnsplashPhotoViewModel
import com.unsplash.pickerandroid.photopicker.UnsplashPhotoPicker
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import com.unsplash.pickerandroid.photopicker.presentation.UnsplashPickerActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var unsplashLists: ArrayList<Unsplash>
    private lateinit var unsplashMainLists: ArrayList<Unsplash>
    private lateinit var unsplashPhotoViewModel: UnsplashPhotoViewModel

    private lateinit var unsplashAdapter: UnsplashAdapter

    private var TAG = "메인 엑티비티"
    private var check: Boolean = false
    private var id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // openAPI init.
        UnsplashPhotoPicker.init(
            application, // application
            getString(R.string.unsplash_access_key),
            getString(R.string.unsplash_secrey_key)
        )

        // recyclerview 초기화 및 어댑터 적용.
        showAPI_list.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        showAPI_list.setHasFixedSize(true)
        unsplashAdapter = UnsplashAdapter(this)
        unsplashAdapter.itemClick = object: UnsplashAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                // 어댑터 아이템뷰 클릭 리스너 -> 이미지 삭제 다이얼로그
                val unsplash = Unsplash(unsplashMainLists.get(position).un_id,unsplashMainLists.get(position).un_url)
                deleteDialog(unsplash)
            }
        }
        showAPI_list.adapter = unsplashAdapter

        // viewModel room repository 옵저버 패턴 적용 -> 어댑터 리스트에 데이터 저장.
        unsplashPhotoViewModel = ViewModelProviders.of(this).get(UnsplashPhotoViewModel::class.java)
        unsplashPhotoViewModel.getAll().observe(this, Observer<List<Unsplash>> { unsplashes ->
            unsplashAdapter.setListOfPhotos(unsplashes!!)

            unsplashMainLists = ArrayList()
            unsplashMainLists.addAll(unsplashes)
        })

        // api 실행 텍스트뷰 리스너
        connectAPI.setOnClickListener {
            startActivityForResult(
                UnsplashPickerActivity.getStartingIntent(
                    this,
                    !check
                ), MainActivity.REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            // 사진 받기.
            val photos: ArrayList<UnsplashPhoto>? = data?.getParcelableArrayListExtra(
                UnsplashPickerActivity.EXTRA_PHOTOS)
            // 객체 저장용 array로 변환.
            if (photos != null) {
                unsplashLists = ArrayList()
                for (i in photos.indices) {
                    val unsplash = Unsplash(id,photos.get(i).urls.small)
                    unsplashLists.add(unsplash)
                }
            }

            /*
            개발 프로세스

            이 사진들을 room 구조 사용해서 sqlite에 저장해준다.
            그럴려면 객체로 저장해주고 repository로 옵저버 패턴 작동하도록 구현해야함.
            이 구조를 제대로 파악하기 먼저 할지 뷰에 이미지 보여지는지 먼저 정하자.
             */

            Toast.makeText(this, "number of selected photos: " + photos?.size, Toast.LENGTH_SHORT).show()

            // 이미지 저장확인 로그 및 내장DB에 insert
            for (i in unsplashLists.indices) {
                Log.d(TAG,"${i}번째 이미지 ${unsplashLists.get(i)}")
                unsplashPhotoViewModel.insert(unsplashLists.get(i))
            }
        }
    }

    private fun deleteDialog(unsplash: Unsplash) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete selected unsplash?")
            .setNegativeButton("NO") { _, _ -> }
            .setPositiveButton("YES") { _, _ ->
                unsplashPhotoViewModel.delete(unsplash)
            }
        builder.show()
    }

    companion object {
        private const val REQUEST_CODE = 123
    }
}