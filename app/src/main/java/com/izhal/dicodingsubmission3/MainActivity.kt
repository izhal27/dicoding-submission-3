package com.izhal.dicodingsubmission3

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
  private lateinit var adapter: UserAdapter
  private lateinit var mainViewModel: MainViewModel

  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    adapter = UserAdapter()
    adapter.notifyDataSetChanged()

    listUser.layoutManager = LinearLayoutManager(this)
    listUser.adapter = adapter

    mainViewModel =
      ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)


    mainViewModel.getUsers().observe(this, Observer { users ->
      if (users != null) {
        showLoading(false)

        if (users.size == 0) {
          showTxtListEmpty(true)
          txtEmptyList.text = "User not found"
        } else {
          showTxtListEmpty(false)
          adapter.setData(users)
        }
      }
    })
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.menu, menu)

    val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
    val searchView = menu.findItem(R.id.searchUser).actionView as SearchView

    searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
    searchView.queryHint = resources.getString(R.string.search_hint)
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

      override fun onQueryTextSubmit(query: String): Boolean {
        if (query.isEmpty()) return false

        adapter.clearData()
        showLoading(true)
        showTxtListEmpty(false)
        mainViewModel.setUser(query)

        return true
      }

      override fun onQueryTextChange(newText: String): Boolean {
        return false
      }
    })

    return true
  }

  private fun showLoading(state: Boolean) {
    progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
  }

  private fun showTxtListEmpty(state: Boolean) {
    txtEmptyList.visibility = if (state) View.VISIBLE else View.INVISIBLE
  }
}