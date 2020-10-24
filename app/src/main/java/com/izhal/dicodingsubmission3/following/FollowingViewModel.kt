package com.izhal.dicodingsubmission3.following

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

class FollowingViewModel : ViewModel() {
  companion object {
    private val TAG = FollowingViewModel::class.java.simpleName
  }

  private var listFollowing = MutableLiveData<ArrayList<User>>()

  fun setLogin(login: String) {
    val users = ArrayList<User>()

    val client = AsyncHttpClient()
    client.setUserAgent("Accept: application/vnd.github.v3+json")

    val url = " https://api.github.com/users/$login/following"

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
                id = jsonObj.getInt("id"),
                login = jsonObj.getString("login"),
                avatarUrl = jsonObj.getString("avatar_url"),
                url = jsonObj.getString("url"),
                htmlUrl = jsonObj.getString("html_url")
              )
            )
          }

          listFollowing.postValue(users)
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

  fun getFollowing(): LiveData<ArrayList<User>> {
    return listFollowing
  }
}