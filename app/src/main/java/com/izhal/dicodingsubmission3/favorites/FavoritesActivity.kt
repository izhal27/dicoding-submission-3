package com.izhal.dicodingsubmission3.favorites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.izhal.dicodingsubmission3.R
import com.izhal.dicodingsubmission3.UserAdapter
import com.izhal.dicodingsubmission3.db.UserHelper
import com.izhal.dicodingsubmission3.detailuser.DetailUserActivity
import com.izhal.dicodingsubmission3.helper.MappingHelper
import com.izhal.dicodingsubmission3.model.UserDetail
import com.izhal.dicodingsubmission3.utils.OnItemClickCallback
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : AppCompatActivity() {
  private lateinit var adapter: FavoritesAdapter
  private lateinit var userHelper: UserHelper
  private lateinit var favoritesViewModel: FavoritesViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_favorites)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = "Favorites"

    adapter = FavoritesAdapter()
    adapter.notifyDataSetChanged()

    listFavorites.layoutManager = LinearLayoutManager(this)
    listFavorites.adapter = adapter

    adapter.setOnItemClickCallback(object : OnItemClickCallback<UserDetail> {
      override fun onItemClicked(data: UserDetail) {
        val intent = Intent(this@FavoritesActivity, DetailUserActivity::class.java)
        intent.putExtra(UserAdapter.EXTRA_LOGIN, data.login)
        startActivity(intent)
      }
    })

    favoritesViewModel = ViewModelProvider(
      this,
      ViewModelProvider.NewInstanceFactory()
    ).get(FavoritesViewModel::class.java)

    userHelper = UserHelper.getInstance(this)
    userHelper.open()

    getAllUserDetails()

    favoritesViewModel.getUserDetails().observe(this, { userDetails ->
      var status = View.VISIBLE

      if (userDetails.size > 0) {
        adapter.setUserDetails(userDetails)
        status = View.INVISIBLE
      } else {
        adapter.clearUserDetails()
      }

      adapter.notifyDataSetChanged()
      imgEmptyFavorites.visibility = status
    })
  }

  private fun getAllUserDetails() {
    val userDetails = MappingHelper.mapCursorToArrayList(userHelper.getAll())
    favoritesViewModel.setUserDetails(userDetails)
  }

  override fun onDestroy() {
    super.onDestroy()
    userHelper.close()
  }

  override fun onResume() {
    super.onResume()
    getAllUserDetails()
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return super.onSupportNavigateUp()
  }
}