package com.izhal.dicodingsubmission3.detailuser

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.izhal.dicodingsubmission3.R
import com.izhal.dicodingsubmission3.followers.FollowersFragment
import com.izhal.dicodingsubmission3.utils.STATUS_FOLLOW

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
  FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
  companion object {
    const val EXTRA_LOGIN = "extra_login"
  }

  private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_2)

  override fun getCount(): Int = TAB_TITLES.size

  override fun getItem(position: Int): Fragment {
    var fragment: Fragment? = null

    val login = (context as DetailUserActivity).login
    val bundle = Bundle()
    bundle.putString(EXTRA_LOGIN, login)

    when (position) {
      0 -> {
        fragment = login?.let {
          FollowersFragment(it, STATUS_FOLLOW.FOLLOWERS)
        }
        fragment?.arguments = bundle
      }
      1 -> {
        fragment = login?.let {
          FollowersFragment(it, STATUS_FOLLOW.FOLLOWING)
        }
        fragment?.arguments = bundle
      }
    }

    return fragment as Fragment
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return context.resources.getString(TAB_TITLES[position])
  }
}