package com.my.search.ui.search_page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.my.search.common.LoadMoreOnScrollListener
import com.my.search.MainActivity
import com.my.search.R
import com.my.search.common.SearchRepository
import java.lang.ref.WeakReference

class SearchPageFragment : Fragment() {

    companion object {
        const val TAG = "SearchPageFragment"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchPageItemAdapter
    private lateinit var swipeRefreshLayout : SwipeRefreshLayout
    private lateinit var singleColumnLayoutManager: LinearLayoutManager
    private lateinit var dualColumnLayoutManager: GridLayoutManager
    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(SearchPageViewModel::class.java)}
    private lateinit var mainActivity: WeakReference<MainActivity>
    private var column: Int = 1
    private var loadMoreOnScrollListener: LoadMoreOnScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_page_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = WeakReference(activity as? MainActivity)
        recyclerView = view.findViewById(R.id.item_recycler_view)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        singleColumnLayoutManager = LinearLayoutManager(view.context).also { it.orientation = LinearLayoutManager.VERTICAL }
        dualColumnLayoutManager = GridLayoutManager(view.context, 2)
        adapter = SearchPageItemAdapter(viewModel.itemList, R.layout.search_page_item_fragment)
        recyclerView.layoutManager = singleColumnLayoutManager
        recyclerView.adapter = adapter

        viewModel.searchLiveData.observe(viewLifecycleOwner, Observer { result ->
            Log.e(TAG, "searchLiveDataObserve: ${result}")
            val items = result.getOrNull()
            if(items != null) {
                val count = viewModel.itemList.size
                viewModel.itemList.clear()
                viewModel.itemList.addAll(items)
                loadMoreOnScrollListener?.resetState()
                adapter.notifyItemRangeRemoved(0, count)
                adapter.notifyItemRangeInserted(0, viewModel.itemList.size)
                mainActivity.get()?.searchOver()
            } else {
                Toast.makeText(activity, "未能查询", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })

        swipeRefreshLayout.setColorSchemeResources(R.color.black)
        swipeRefreshLayout.setOnRefreshListener {
            mainActivity.get()?.refresh(swipeRefreshLayout)
        }

        updateOnScrollListener()

        mainActivity.get()?.search("")
    }

    private fun updateOnScrollListener() {
        recyclerView.clearOnScrollListeners()
        loadMoreOnScrollListener = object : LoadMoreOnScrollListener(
            if(column == 1) singleColumnLayoutManager else dualColumnLayoutManager
        ) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                val itemsLiveData = SearchRepository.search(viewModel.getSearchQuery() ?: "", 10)
                itemsLiveData.observe(viewLifecycleOwner, Observer { result ->
                    val items = result.getOrNull()
                    if(items != null) {
                        val pos = viewModel.itemList.size - 1
                        val count = items.size
                        viewModel.itemList.addAll(items)
                        adapter.notifyItemRangeInserted(pos, count)
                        Toast.makeText(activity, "已加载更多数据", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "未能加载更多数据", Toast.LENGTH_SHORT).show()
                        result.exceptionOrNull()?.printStackTrace()
                    }
                    itemsLiveData.removeObservers(viewLifecycleOwner)
                })
            }
            override fun doPullToRefresh() {
            }
        }
        recyclerView.addOnScrollListener(
            loadMoreOnScrollListener as OnScrollListener
        )
    }


    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if(menuVisible) mainActivity.get()?.columnIconChange(column)
    }

    fun columnChange() : Int {
        column = if(column == 1) 2 else 1
        when(column) {
            1 -> recyclerView.layoutManager = singleColumnLayoutManager
            2 -> recyclerView.layoutManager = dualColumnLayoutManager
        }
        adapter.notifyItemRangeChanged(0, adapter.itemCount ?: 0)
        updateOnScrollListener()
        return column
    }
}