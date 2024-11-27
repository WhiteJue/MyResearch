package com.my.search.ui.details_page

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.my.search.R
import com.my.search.model.SearchItem
import java.lang.ref.WeakReference

class DetailsWebActivity : AppCompatActivity() {

    companion object {
        const val ERROR_MSG = "页面加载错误，点击刷新重试"
        const val KEY_ITEM = "item"
        fun open(context: Context, item: SearchItem) {
            val intent = Intent(context, DetailsWebActivity::class.java).apply {
                putExtra(KEY_ITEM, item)
            }
            context.startActivity(intent)
        }
    }

    private var item : SearchItem? = null
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var backIButton: ImageButton
    private lateinit var closeIButton: ImageButton
    private lateinit var titleTextView: TextView
    private lateinit var errorLayout: LinearLayout

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_web_activity)
        item = IntentCompat.getParcelableExtra(intent, KEY_ITEM, SearchItem::class.java)
        webView = findViewById(R.id.web_view)
        progressBar = findViewById(R.id.web_progress_bar)
        backIButton = findViewById(R.id.web_bar_back_image_button)
        closeIButton = findViewById(R.id.web_bar_close_image_button)
        titleTextView = findViewById(R.id.web_bar_title_text_view)
        errorLayout = findViewById(R.id.web_error_layout)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = DetailsWebViewClient(WeakReference(this))
        webView.webChromeClient = DetailsWebChromeClient(WeakReference(this))

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                tryBack()
            }
        })

        backIButton.setOnClickListener {
            tryBack()
        }

        closeIButton.setOnClickListener {
            tryClose()
        }

        errorLayout.setOnClickListener {
            reload()
        }

        if(item != null) load(item!!.targetUrl)
    }

    fun tryBack() {
        if(webView.canGoBack()) {
            webView.goBack()
        } else {
            tryClose()
        }
    }

    fun tryClose() {
        finish()
    }

    fun setBarTitle(title: String) {
        titleTextView.text = title
    }

    fun load(url: String) {
        webView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
        webView.loadUrl(url)
    }

    fun reload() {
        webView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
        webView.reload()
    }

    fun onPageFinished() {
        if(errorLayout.visibility == View.VISIBLE) return
        progressBar.visibility = View.GONE
        errorLayout.visibility = View.GONE
        webView.visibility = View.VISIBLE
    }

    fun onReceivedError(error: WebResourceError?) {
        errorLayout.findViewById<TextView>(R.id.web_error_text_view).text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            "$ERROR_MSG\n${error?.description ?: ""}" else ERROR_MSG
        errorLayout.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        webView.visibility = View.GONE
    }

    fun interceptRequest(url: String) {
        Toast.makeText(this, "已拦截跳转到外部APP", Toast.LENGTH_SHORT).show()
    }

}