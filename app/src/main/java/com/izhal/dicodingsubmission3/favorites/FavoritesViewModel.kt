package com.izhal.dicodingsubmission3.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.izhal.dicodingsubmission3.model.UserDetail

class FavoritesViewModel : ViewModel() {
  private val listUserDetails = MutableLiveData<ArrayList<UserDetail>>()

  fun setUserDetails(userDetails: ArrayList<UserDetail>?) {
    listUserDetails.postValue(userDetails)
  }

  fun getUserDetails(): LiveData<ArrayList<UserDetail>> {
    return listUserDetails
  }
}