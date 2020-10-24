package com.izhal.dicodingsubmission3.followers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.izhal.dicodingsubmission3.R
import com.izhal.dicodingsubmission3.detailuser.SectionsPagerAdapter
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment() {
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
    var login: String? = null
    arguments?.let {
      login = it.get(SectionsPagerAdapter.EXTRA_LOGIN) as String?
    }

    adapter = FollowersAdapter()
    adapter.notifyDataSetChanged()

    listFollowers.layoutManager = LinearLayoutManager(this.context)
    listFollowers.adapter = adapter

    followersViewModel = ViewModelProvider(
      this,
      ViewModelProvider.NewInstanceFactory()
    ).get(FollowersViewModel::class.java)

    login?.let { followersViewModel.setLogin(it) }

    activity?.let {
      followersViewModel.getFollowers().observe(it, { followers ->
        if (followers != null) {
          adapter.setData(followers)
        }

        showLoading(false)
      })
    }
  }

  private fun showLoading(state: Boolean) {
    progressBarFollowers.visibility = if (state) View.VISIBLE else View.INVISIBLE
  }
}