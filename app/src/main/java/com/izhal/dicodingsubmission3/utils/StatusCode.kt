package com.izhal.dicodingsubmission3.utils

class StatusCode {
  companion object {
    fun errorMessage(statusCode: Int, error: Throwable?): String {
      return  when (statusCode) {
        401 -> "$statusCode : Bad Request"
        403 -> "$statusCode : Forbidden"
        404 -> "$statusCode : Not Found"
        else -> "$statusCode : ${error?.message}"
      }
    }
  }
}