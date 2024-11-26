package com.my.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.my.search.model.SearchItem
import retrofit2.http.Url
import java.lang.ref.WeakReference

class DetailsWebActivity : AppCompatActivity() {
    private var item : SearchItem? = null
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_web_activity)
        item = IntentCompat.getParcelableExtra(intent, KEY_ITEM, SearchItem::class.java)
        webView = findViewById(R.id.web_view)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = DetailsWebClient(WeakReference(this))
        if(item != null) webView.loadUrl(item!!.targetUrl)
    }

    fun interceptRequest(url: String) {
        Toast.makeText(this, "已拦截跳转到外部APP", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val KEY_ITEM = "item"
        fun open(context: Context, item: SearchItem) {
            val intent = Intent(context, DetailsWebActivity::class.java).apply {
                putExtra(KEY_ITEM, item)
            }
            context.startActivity(intent)
        }
    }
}