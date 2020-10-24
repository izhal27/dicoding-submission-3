package com.izhal.dicodingsubmission3

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.activity_webview.progressBar

class WebViewActivity : AppCompatActivity() {
  companion object {
    const val EXTRA_REPO_URL = "extra_repo_url"
  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_webview)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    containerWebview.bringChildToFront(progressBar)

    val repoUrl = intent.getStringExtra(EXTRA_REPO_URL)

    webview.webViewClient = WebViewClient()
    webview.settings.javaScriptEnabled = true
    webview.webViewClient = (object : WebViewClient() {
      override fun onPageFinished(view: WebView?, url: String?) {
        progressBar.visibility = View.INVISIBLE
        super.onPageFinished(view, url)
      }
    })

    repoUrl?.let { webview.loadUrl(it) }
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }
}