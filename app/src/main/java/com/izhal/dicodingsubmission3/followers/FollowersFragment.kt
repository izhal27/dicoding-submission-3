package com.izhal.dicodingsubmission3.followers

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.izhal.dicodingsubmission3.R
import com.izhal.dicodingsubmission3.UserAdapter
import com.izhal.dicodingsubmission3.detailuser.DetailUserActivity
import com.izhal.dicodingsubmission3.model.User
import com.izhal.dicodingsubmission3.utils.OnItemClickCallback
import com.izhal.dicodingsubmission3.utils.STATUS_FOLLOW
import com.izhal.dicodingsubmission3.webview.WebViewActivity
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment(private val login: String, private val statusFollow: STATUS_FOLLOW) : Fragment() {
  private lateinit var followersViewModel: FollowersViewModel
  private lateinit var adapter: FollowersAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_followers, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    showLoading(true)

    adapter = FollowersAdapter()
    adapter.notifyDataSetChanged()

    listFollowers.layoutManager = LinearLayoutManager(this.context)
    listFollowers.adapter = adapter

    adapter.setOnButtonDetailClickCallback(object : OnItemClickCallback<User> {
      override fun onClicked(data: User) {
        val intent = Intent(context, DetailUserActivity::class.java)
        intent.putExtra(UserAdapter.EXTRA_LOGIN, data.login)
        startActivity(intent)
      }
    })

    adapter.setOnButtonRepoClickCallback(object : OnItemClickCallback<User> {
      override fun onClicked(data: User) {
        val intent = Intent(context, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.EXTRA_REPO_URL, data.htmlUrl)
        startActivity(intent)
      }
    })

    followersViewModel = ViewModelProvider(
      this,
      ViewModelProvider.NewInstanceFactory()
    ).get(FollowersViewModel::class.java)

    when (this.statusFollow) {
      STATUS_FOLLOW.FOLLOWERS -> {
        followersViewModel.setFollowersLogin(this.login)
      }

      STATUS_FOLLOW.FOLLOWING -> {
        followersViewModel.setFollowingLogin(this.login)
      }
    }

    activity?.let {
      followersViewModel.getUsers().observe(it, { users ->
        if (users != null) {
          adapter.setData(users)
        }

        showLoading(false)
      })
    }
  }

  private fun showLoading(state: Boolean) {
    progressBarFollowers.visibility = if (state) View.VISIBLE else View.INVISIBLE
  }
}