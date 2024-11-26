package com.my.search

import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import java.lang.ref.WeakReference

class DetailsWebClient(val activity: WeakReference<DetailsWebActivity>) : WebViewClient() {
    companion object {
        const val TAG = "DetailsWebClient"
    }

    private fun urlSchemeNotHttp(scheme: String?) : Boolean {
        if(scheme.isNullOrEmpty()) return false
        return scheme != "http" && scheme != "https"
    }
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Log.d(TAG, "url: ${request?.url}")
        if(urlSchemeNotHttp(request?.url?.scheme)) {
            activity.get()?.interceptRequest(request?.url.toString())
            return true
        }
        return super.shouldOverrideUrlLoading(view, request)
    }
}