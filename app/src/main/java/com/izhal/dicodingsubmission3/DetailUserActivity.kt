package com.izhal.dicodingsubmission3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_detail_user.*
import kotlinx.android.synthetic.main.toolbar.*

class DetailUserActivity : AppCompatActivity() {
  private lateinit var detailUserViewModel: DetailUserViewModel
  var login: String? = null

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
      }
    })
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