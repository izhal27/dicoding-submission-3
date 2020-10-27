package com.izhal.dicodingsubmission3

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.izhal.dicodingsubmission3.detailuser.DetailUserActivity
import com.izhal.dicodingsubmission3.favorites.FavoritesActivity
import com.izhal.dicodingsubmission3.model.User
import com.izhal.dicodingsubmission3.settings.SettingsActivity
import com.izhal.dicodingsubmission3.utils.OnItemClickCallback
import com.izhal.dicodingsubmission3.webview.WebViewActivity
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

    adapter.setOnButtonDetailClickCallback(object : OnItemClickCallback<User> {
      override fun onClicked(data: User) {

        Log.d(MainActivity::class.java.simpleName, data.login)


        val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
        intent.putExtra(UserAdapter.EXTRA_LOGIN, data.login)
        startActivity(intent)
      }
    })

    adapter.setOnButtonRepoClickCallback(object : OnItemClickCallback<User> {
      override fun onClicked(data: User) {
        val intent = Intent(this@MainActivity, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.EXTRA_REPO_URL, data.htmlUrl)
        startActivity(intent)
      }
    })

    mainViewModel =
      ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)


    mainViewModel.getUsers().observe(this, { users ->
      if (users != null) {
        showLoading(false)

        if (users.size == 0) {
          showTxtListEmpty(true)
          imgGithub.visibility = View.GONE
          txtEmptyList.text = "User not found"
        } else {
          showTxtListEmpty(false)
          adapter.setData(users)
        }
      }
    })
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu, menu)

    val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
    val searchView = menu.findItem(R.id.searchUserMenu).actionView as SearchView

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

    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.favoritesMenu -> {
        val intent = Intent(this@MainActivity, FavoritesActivity::class.java)
        startActivity(intent)
      }

      R.id.settingMenu -> {
        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
        startActivity(intent)
      }
    }
    return super.onContextItemSelected(item)
  }

  private fun showLoading(state: Boolean) {
    progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
  }

  private fun showTxtListEmpty(state: Boolean) {
    containerEmptyList.visibility = if (state) View.VISIBLE else View.INVISIBLE
  }
}