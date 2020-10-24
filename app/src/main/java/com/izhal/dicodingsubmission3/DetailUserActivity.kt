package com.izhal.dicodingsubmission3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_detail_user.*
import kotlinx.android.synthetic.main.toolbar.*

class DetailUserActivity : AppCompatActivity() {
  private lateinit var detailUserViewModel: DetailUserViewModel
  var login: String? = null

  private var statusFavorite = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail_user)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    containerImage.bringChildToFront(progressBar)

    val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
    viewPager.adapter = sectionsPagerAdapter
    tabs.setupWithViewPager(viewPager)

    login = intent.getStringExtra(UserAdapter.EXTRA_LOGIN)

    detailUserViewModel = ViewModelProvider(
      this,
      ViewModelProvider.NewInstanceFactory()
    ).get(DetailUserViewModel::class.java)
    login?.let { detailUserViewModel.setLogin(it) }
    detailUserViewModel.getUserDetail().observe(this, { userDetail ->
      if (userDetail != null) {
        imgAvatarDetail.loadImage(userDetail.avatar_url)

        txtName.text = userDetail.name
        txtUsername.text = userDetail.login
        txtLocation.text = userDetail.location
        txtFollowing.text = userDetail.following.toString()
        txtFollowers.text = userDetail.followers.toString()

        progressBar.visibility = View.INVISIBLE
        containerName.visibility = View.VISIBLE
        btnFavorite.visibility = View.VISIBLE
      }
    })

    btnFavorite.setOnClickListener {
      statusFavorite = !statusFavorite
      setButtonStatusFavorite(statusFavorite)
    }
  }

  private fun setButtonStatusFavorite(statusFavorite: Boolean) {
    if (statusFavorite) {
      btnFavorite.setImageResource(R.drawable.ic_heart_full_white)
      Toast.makeText(this, "User berhasil ditambahkan ke daftar Favorit", Toast.LENGTH_SHORT).show()
    } else {
      btnFavorite.setImageResource(R.drawable.ic_heart_white)
      Toast.makeText(this, "User berhasil dihapus dari daftar Favorit", Toast.LENGTH_SHORT).show()
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