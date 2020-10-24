package com.izhal.dicodingsubmission3

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {
  companion object {
    private val TAG = MainViewModel::class.java.simpleName
  }

  private val listUsers = MutableLiveData<ArrayList<User>>()

  fun setUser(user: String) {
    val users = ArrayList<User>()

    val client = AsyncHttpClient()
    client.setUserAgent("Accept: application/vnd.github.v3+json")

    val url = "https://api.github.com/search/users?q=$user"

    client.get(url, object : AsyncHttpResponseHandler() {
      @SuppressLint("SetTextI18n")
      override fun onSuccess(
        statusCode: Int,
        headers: Array<out Header>?,
        responseBody: ByteArray?
      ) {
        try {
          val result = responseBody?.let { String(it) }
          val res = result?.let { JSONObject(result) }

          val jsonArray = res?.getJSONArray("items")

          for (i in 0 until (jsonArray?.length() ?: 0)) {
            val jsonObj = jsonArray!!.getJSONObject(i)
            users.add(
              User(
                id = jsonObj.getInt("id"),
                login = jsonObj.getString("login"),
                avatar_url = jsonObj.getString("avatar_url"),
                url = jsonObj.getString("url"),
                html_url = jsonObj.getString("html_url")
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