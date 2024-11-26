package com.my.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.my.search.ui.SearchPageFragment
import com.my.search.ui.SearchPageViewModel
import com.my.search.ui.SearchViewPageAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var tab : TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var pageAdapter: SearchViewPageAdapter
    private lateinit var dualButton : ImageButton
    private lateinit var progressBar: ProgressBar
    private var swipeRefreshLayout : SwipeRefreshLayout? = null
    private val viewModel by lazy { ViewModelProvider(this).get(SearchPageViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initViewPager()
        initTab()
    }

    private fun initViewPager() {
        progressBar = findViewById<ProgressBar>(R.id.main_progress_bar)
        tab = findViewById<TabLayout>(R.id.main_tab_layout)
        viewPager = findViewById<ViewPager2>(R.id.main_view_pager)
        pageAdapter = SearchViewPageAdapter(this)
        viewPager.adapter = pageAdapter
        TabLayoutMediator(tab, viewPager) { tab, position ->
            tab.text = "Tab ${position}"
        }.attach()
    }

    private fun initTab() {
        dualButton = findViewById<ImageButton>(R.id.main_tab_dual_button)
        dualButton.setOnClickListener {
            columnChange()
        }
    }

    fun columnChange() {
        currentPageFragment()?.columnChange()?.also { columnIconChange(it) }
    }

    fun columnIconChange(column: Int) {
        dualButton.setImageResource(if(column == 1) R.drawable.single_column else R.drawable.dual_column)
    }

    private fun currentPageFragment() : SearchPageFragment? {
        return supportFragmentManager.findFragmentByTag("f" + viewPager.currentItem) as? SearchPageFragment
    }

    fun search() {
        hideSoftInput()
        viewPager.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        viewModel.search("1")
    }

    fun searchOver() {
        progressBar.visibility = View.GONE
        viewPager.visibility = View.VISIBLE
        swipeRefreshLayout?.isRefreshing = false
    }

    fun refresh(swipe: SwipeRefreshLayout) {
        swipeRefreshLayout = swipe
        swipeRefreshLayout?.isRefreshing = true
        viewModel.refresh()
    }

    private fun hideSoftInput() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}