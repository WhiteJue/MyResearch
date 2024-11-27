package com.my.search.ui.details_page

import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import java.lang.ref.WeakReference

class DetailsWebViewClient(val activity: WeakReference<DetailsWebActivity>) : WebViewClient() {
    companion object {
        const val TAG = "DetailsWebClient"
    }

    private fun urlSchemeNotHttp(scheme: String?) : Boolean {
        if(scheme.isNullOrEmpty()) return false
        return scheme != "http" && scheme != "https"
    }

    private fun urlPatternNotAllowed(url: String?) : Boolean {
        if(url.isNullOrEmpty()) return false
        if(url.contains("www.zhihu.com/oia/answers/")) return true
        return false
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Log.d(TAG, "url: ${request?.url}")
        if(urlSchemeNotHttp(request?.url?.scheme) || urlPatternNotAllowed(request?.url.toString())) {
            activity.get()?.interceptRequest(request?.url.toString())
            return true
        }
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        activity.get()?.onPageFinished()
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        activity.get()?.onReceivedError(error)
    }
}