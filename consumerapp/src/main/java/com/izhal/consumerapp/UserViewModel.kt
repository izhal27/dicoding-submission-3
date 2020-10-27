package com.izhal.consumerapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.izhal.consumerapp.model.UserDetail

class UserViewModel : ViewModel() {
  private val listUserDetails = MutableLiveData<ArrayList<UserDetail>>()

  fun setUserDetails(userDetails: ArrayList<UserDetail>?) {
    listUserDetails.postValue(userDetails)
  }

  fun getUserDetails(): LiveData<ArrayList<UserDetail>> {
    return listUserDetails
  }
}