package com.my.search.ui.details_page

import android.webkit.WebChromeClient
import android.webkit.WebView
import java.lang.ref.WeakReference

class DetailsWebChromeClient(val activity: WeakReference<DetailsWebActivity>) : WebChromeClient() {
    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        activity.get()?.setBarTitle(title ?: "")
    }
}