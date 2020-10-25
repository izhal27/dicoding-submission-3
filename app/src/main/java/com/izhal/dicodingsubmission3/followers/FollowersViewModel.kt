package com.izhal.dicodingsubmission3.followers

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.izhal.dicodingsubmission3.utils.StatusCode
import com.izhal.dicodingsubmission3.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersViewModel : ViewModel() {
  companion object {
    private val TAG = FollowersViewModel::class.java.simpleName
  }

  private var listUsers = MutableLiveData<ArrayList<User>>()

  fun setFollowersLogin(login: String) {
    val url = " https://api.github.com/users/$login/followers"
    this.getData(url)
  }

  fun setFollowingLogin(login: String) {
    val url = " https://api.github.com/users/$login/following"
    this.getData(url)
  }

  private fun getData(url: String) {
    val users = ArrayList<User>()

    val client = AsyncHttpClient()
    client.setUserAgent("Accept: application/vnd.github.v3+json")

    client.get(url, object : AsyncHttpResponseHandler() {
      @SuppressLint("SetTextI18n")
      override fun onSuccess(
        statusCode: Int,
        headers: Array<out Header>?,
        responseBody: ByteArray?
      ) {
        try {
          val result = responseBody?.let { String(it) }
          val jsonArray = result?.let { JSONArray(result) }

          for (i in 0 until (jsonArray?.length() ?: 0)) {
            val jsonObj = jsonArray!!.getJSONObject(i)
            users.add(
              User(
                g_id = jsonObj.getInt("id"),
                login = jsonObj.getString("login"),
                avatarUrl = jsonObj.getString("avatar_url"),
                url = jsonObj.getString("url"),
                htmlUrl = jsonObj.getString("html_url")
              )
            )
          }

          listUsers.postValue(users)
        } catch (e: Exception) {
          e.printStackTrace()
          e.message?.let { Log.d(TAG, it) }
        }
      }

      override fun onFailure(
        statusCode: Int,
        headers: Array<out Header>?,
        responseBody: ByteArray?,
        error: Throwable?
      ) {
        val errorMessage = StatusCode.errorMessage(statusCode, error)

        val res = responseBody?.let { String(it) }
        Log.d(TAG, "Status: $errorMessage\nResponse: $res")
      }
    })
  }

  fun getUsers(): LiveData<ArrayList<User>> {
    return listUsers
  }
}