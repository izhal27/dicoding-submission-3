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
import com.izhal.dicodingsubmission3.detailuser.SectionsPagerAdapter
import com.izhal.dicodingsubmission3.model.User
import com.izhal.dicodingsubmission3.utils.OnItemClickCallback
import com.izhal.dicodingsubmission3.utils.STATUS_FOLLOW
import com.izhal.dicodingsubmission3.webview.WebViewActivity
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment() : Fragment() {
  private lateinit var followersViewModel: FollowersViewModel
  private lateinit var adapter: FollowersAdapter
  private var login: String? = null
  private lateinit var status: STATUS_FOLLOW

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_followers, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    login = activity?.intent?.getStringExtra(SectionsPagerAdapter.EXTRA_LOGIN)
    val statusExtra = activity?.intent?.getStringExtra(SectionsPagerAdapter.EXTRA_STATUS)
    status = if (statusExtra == STATUS_FOLLOW.FOLLOWERS.toString()) STATUS_FOLLOW.FOLLOWERS else STATUS_FOLLOW.FOLLOWING

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

    when (this.status) {
      STATUS_FOLLOW.FOLLOWERS -> {
        this.login?.let { followersViewModel.setFollowersLogin(it) }
      }

      STATUS_FOLLOW.FOLLOWING -> {
        this.login?.let { followersViewModel.setFollowingLogin(it) }
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