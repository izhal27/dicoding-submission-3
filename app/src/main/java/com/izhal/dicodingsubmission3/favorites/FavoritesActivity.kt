package com.izhal.dicodingsubmission3.favorites

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.izhal.dicodingsubmission3.R
import com.izhal.dicodingsubmission3.UserAdapter
import com.izhal.dicodingsubmission3.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.izhal.dicodingsubmission3.detailuser.DetailUserActivity
import com.izhal.dicodingsubmission3.helper.MappingHelper
import com.izhal.dicodingsubmission3.model.UserDetail
import com.izhal.dicodingsubmission3.utils.OnItemClickCallback
import com.izhal.dicodingsubmission3.webview.WebViewActivity
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {
  private lateinit var adapter: FavoritesAdapter
  private lateinit var favoritesViewModel: FavoritesViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_favorites)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = "Favorites"
    containerFavorites.bringChildToFront(progressBar)

    adapter = FavoritesAdapter()
    adapter.notifyDataSetChanged()

    listFavorites.layoutManager = LinearLayoutManager(this)
    listFavorites.adapter = adapter

    adapter.setOnButtonDetailClickCallback(object : OnItemClickCallback<UserDetail> {
      override fun onClicked(data: UserDetail) {
        val intent = Intent(this@FavoritesActivity, DetailUserActivity::class.java)
        intent.putExtra(UserAdapter.EXTRA_LOGIN, data.login)
        startActivity(intent)
      }
    })

    adapter.setOnButtonRepoClickCallback(object : OnItemClickCallback<UserDetail> {
      override fun onClicked(data: UserDetail) {
        val intent = Intent(this@FavoritesActivity, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.EXTRA_REPO_URL, data.htmlUrl)
        startActivity(intent)
      }
    })

    favoritesViewModel = ViewModelProvider(
      this,
      ViewModelProvider.NewInstanceFactory()
    ).get(FavoritesViewModel::class.java)

    val handlerThread = HandlerThread("DataObserver")
    handlerThread.start()
    val handler = Handler(handlerThread.looper)
    val myObserver = object : ContentObserver(handler) {
      override fun onChange(self: Boolean) {
        getUsersAsync()
      }
    }

    contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

    favoritesViewModel.getUserDetails().observe(this, { userDetails ->
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
        favoritesViewModel.setUserDetails(userDetails)
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

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return super.onSupportNavigateUp()
  }
}