package com.izhal.dicodingsubmission3.helper

import android.content.Context
import android.widget.Toast

object Messages {
  fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
  }
}