package com.izhal.dicodingsubmission3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {
  private lateinit var followingViewModel: FollowingViewModel
  private lateinit var adapter: FollowingAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_following, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    showLoading(true)
    var login: String? = null
    arguments?.let {
      login = it.get(SectionsPagerAdapter.EXTRA_LOGIN) as String?
    }

    adapter = FollowingAdapter()
    adapter.notifyDataSetChanged()

    listFollowing.layoutManager = LinearLayoutManager(this.context)
    listFollowing.adapter = adapter

    followingViewModel = ViewModelProvider(
      this,
      ViewModelProvider.NewInstanceFactory()
    ).get(FollowingViewModel::class.java)

    login?.let { followingViewModel.setLogin(it) }

    activity?.let {
      followingViewModel.getFollowing().observe(it, { following ->
        if (following != null) {
          adapter.setData(following)
        }

        showLoading(false)
      })
    }
  }

  private fun showLoading(state: Boolean) {
    progressBarFollowing.visibility = if (state) View.VISIBLE else View.INVISIBLE
  }
}