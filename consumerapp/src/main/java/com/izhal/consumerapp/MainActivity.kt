package com.izhal.consumerapp

import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.izhal.consumerapp.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.izhal.consumerapp.helper.MappingHelper
import com.izhal.consumerapp.helper.Messages
import com.izhal.consumerapp.model.UserDetail
import com.izhal.consumerapp.utils.OnItemClickCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
  private lateinit var adapter: UserAdapter
  private lateinit var viewModel: UserViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    containerUsers.bringChildToFront(progressBar)

    adapter = UserAdapter()
    viewModel = UserViewModel()

    rvUsers.layoutManager = LinearLayoutManager(this)
    rvUsers.adapter = adapter

    adapter.setOnButtonDeleteClickCallback(object : OnItemClickCallback<UserDetail> {
      override fun onClicked(data: UserDetail) {
        if (data.id > 0) {
          val uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + data?.id)
          contentResolver.delete(uriWithId, null, null)

          Messages.showToast(this@MainActivity, "User berhasil dihapus dari daftar Favorit")
        }
      }
    })

    viewModel = ViewModelProvider(
      this,
      ViewModelProvider.NewInstanceFactory()
    ).get(UserViewModel::class.java)


    val handlerThread = HandlerThread("DataObserver")
    handlerThread.start()
    val handler = Handler(handlerThread.looper)
    val myObserver = object : ContentObserver(handler) {
      override fun onChange(self: Boolean) {
        getUsersAsync()
      }
    }

    contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

    viewModel.getUserDetails().observe(this, { userDetails ->
      var status = View.VISIBLE

      if (userDetails.size > 0) {
        adapter.setUserDetails(userDetails)
        status = View.INVISIBLE
      }

      imgEmptyFavorites.visibility = status
    })
  }

  private fun getUsersAsync() {
    GlobalScope.launch(Dispatchers.Main) {
      progressBar.visibility = View.VISIBLE

      val deferredUsers = async(Dispatchers.IO) {
        val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
        MappingHelper.mapCursorToArrayList(cursor)
      }

      val userDetails = deferredUsers.await()
      progressBar.visibility = View.INVISIBLE
      var status = View.VISIBLE

      if (userDetails.size > 0) {
        viewModel.setUserDetails(userDetails)
        status = View.INVISIBLE
      } else {
        adapter.clearData()
      }

      imgEmptyFavorites.visibility = status
    }
  }

  override fun onResume() {
    super.onResume()
    getUsersAsync()
  }
}