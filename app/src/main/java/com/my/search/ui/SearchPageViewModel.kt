package com.my.search.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.my.search.common.SearchRepository
import com.my.search.model.SearchItem

class SearchPageViewModel : ViewModel() {
    companion object {
        const val TAG = "SearchPageViewModel"
    }

    private val searchQueryLiveData = MutableLiveData<String>()
    val itemList = ArrayList<SearchItem>()

    val searchLiveData : LiveData<Result<List<SearchItem>>> = searchQueryLiveData.switchMap { query ->
        Log.d(TAG, "searchLiveDataChange: ${query}")
        SearchRepository.searchTest(query)
    }

    fun search(query: String) {
        Log.d(TAG, "search: ${query}")
        searchQueryLiveData.value = query
    }

    fun refresh() {
        search(searchQueryLiveData.value ?: "")
    }
}