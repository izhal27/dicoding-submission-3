package com.izhal.dicodingsubmission3.detailuser

import android.content.ContentValues
import android.database.SQLException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.izhal.dicodingsubmission3.R
import com.izhal.dicodingsubmission3.UserAdapter
import com.izhal.dicodingsubmission3.db.DatabaseContract
import com.izhal.dicodingsubmission3.db.DatabaseContract.AUTHORITY
import com.izhal.dicodingsubmission3.db.DatabaseContract.SCHEME
import com.izhal.dicodingsubmission3.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.izhal.dicodingsubmission3.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.izhal.dicodingsubmission3.helper.MappingHelper
import com.izhal.dicodingsubmission3.helper.Messages
import com.izhal.dicodingsubmission3.model.UserDetail
import com.izhal.dicodingsubmission3.utils.loadImage
import kotlinx.android.synthetic.main.activity_detail_user.*
import kotlinx.android.synthetic.main.toolbar.*

class DetailUserActivity : AppCompatActivity() {
  companion object {
    private val TAG = DetailUserActivity::class.simpleName
  }

  private lateinit var detailUserViewModel: DetailUserViewModel
  var login: String? = null
  private var userDetail: UserDetail? = null
  private lateinit var uriWithLogin: Uri

  private var statusFavorite = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail_user)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = "Detail User"
    containerImage.bringChildToFront(progressBar)

    val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
    viewPager.adapter = sectionsPagerAdapter
    tabs.setupWithViewPager(viewPager)

    login = intent.getStringExtra(UserAdapter.EXTRA_LOGIN)

    detailUserViewModel = ViewModelProvider(
      this,
      ViewModelProvider.NewInstanceFactory()
    ).get(DetailUserViewModel::class.java)

    uriWithLogin =
      Uri.Builder().scheme(SCHEME).authority(AUTHORITY).appendPath(TABLE_NAME).appendQueryParameter(
        "login",
        this.login
      ).build()

    val cursor = contentResolver.query(uriWithLogin, null, null, null, null)
    cursor?.apply {
      userDetail = MappingHelper.mapCursorToObject(this)
      this.close()
    }

    // jika user sudah ada di database maka,
    // set user di viewmodel sesuai user yang didapat dari database,
    // jika tidak, ambil data user dari server github
    if (userDetail != null) {
      detailUserViewModel.setUserDetail(userDetail)
    } else {
      login?.let { detailUserViewModel.setLogin(it) }
    }

    detailUserViewModel.getUserDetail().observe(this, { userDetail ->
      if (userDetail != null) {
        imgAvatarDetail.loadImage(userDetail.avatarUrl)

        txtName.text = userDetail.name
        txtUsername.text = userDetail.login
        txtLocation.text = userDetail.location
        txtFollowing.text = userDetail.following.toString()
        txtFollowers.text = userDetail.followers.toString()

        progressBar.visibility = View.INVISIBLE
        containerName.visibility = View.VISIBLE

        // jika id lebih dari 0, maka user sudah ada di database
        if (userDetail.id > 0) {
          statusFavorite = true
          toggleFavoriteIcon(statusFavorite)
        }

        btnFavorite.visibility = View.VISIBLE
      }
    })

    btnFavorite.setOnClickListener {
      statusFavorite = !statusFavorite
      setButtonStatusFavorite(statusFavorite)
    }
  }

  private fun setButtonStatusFavorite(status: Boolean) {
    if (status) {
      val userDetail = detailUserViewModel.getCurrentUserDetail()

      if (userDetail != null) {
        val values = ContentValues()
        userDetail.apply {
          values.put(DatabaseContract.UserColumns.G_ID, g_id)
          values.put(DatabaseContract.UserColumns.LOGIN, login)
          values.put(DatabaseContract.UserColumns.AVATAR_URL, avatarUrl)
          values.put(DatabaseContract.UserColumns.URL, url)
          values.put(DatabaseContract.UserColumns.HTML_URL, htmlUrl)
          values.put(DatabaseContract.UserColumns.NAME, name)
          values.put(DatabaseContract.UserColumns.REPOS_URL, reposUrl)
          values.put(DatabaseContract.UserColumns.FOLLOWERS_URL, followersUrl)
          values.put(DatabaseContract.UserColumns.FOLLOWING_URL, followingUrl)
          values.put(DatabaseContract.UserColumns.LOCATION, location)
          values.put(DatabaseContract.UserColumns.BIO, bio)
          values.put(DatabaseContract.UserColumns.FOLLOWERS, followers)
          values.put(DatabaseContract.UserColumns.FOLLOWING, following)
        }

        try {
          contentResolver.insert(CONTENT_URI, values)
        } catch (ex: SQLException) {
          ex.printStackTrace()
          ex.message?.let { Log.d(TAG, it) }
        }

        Messages.showToast(this, "User berhasil ditambahkan ke daftar Favorit")
      }
    } else {
      if (userDetail?.id!! > 0) {
        try {
          val uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + userDetail?.id)
          contentResolver.delete(uriWithId, null, null)
        } catch (ex: SQLException) {
          ex.printStackTrace()
          ex.message?.let { Log.d(TAG, it) }
        }

        Messages.showToast(this, "User berhasil dihapus dari daftar Favorit")
      }
    }

    toggleFavoriteIcon(status)
  }

  private fun toggleFavoriteIcon(status: Boolean) {
    if (status) {
      btnFavorite.setImageResource(R.drawable.ic_heart_full_white)
    } else {
      btnFavorite.setImageResource(R.drawable.ic_heart_white)
    }
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }

  override fun onBackPressed() {
    if (viewPager.currentItem == 0) {
      super.onBackPressed()
    } else {
      viewPager.currentItem = viewPager.currentItem - 1
    }
  }
}