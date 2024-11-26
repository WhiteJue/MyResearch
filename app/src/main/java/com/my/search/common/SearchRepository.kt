package com.my.search.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.my.search.model.SearchItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

object SearchRepository {
    fun search(query: String): LiveData<Result<List<SearchItem>>> = liveData(Dispatchers.IO) {
        //子线程中执行
        val result = try {
            //调用API
            val placeResponse = SearchNetwork.search(query)
            if(placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<SearchItem>>(e)
        }
        //emit方法构建LiveData
        emit(result)
    }

    //Mock
    fun searchTest(query: String): LiveData<Result<List<SearchItem>>> = liveData(Dispatchers.IO) {
        delay(1000 * 3)
        val itemList : List<SearchItem> = mutableListOf<SearchItem>().also {
            val il = it
            repeat(5) {
                il.addAll(
                    listOf(
                        SearchItem(
                            "123",
                            "https://cdn-images-1.medium.com/max/2000/1*wUcMnW5qarNCTDk7T7918g.jpeg",
                            0,
                            "https://www.zhihu.com/topic/20023724/unanswered?utm_id=0"
                        ),
                        SearchItem(
                            "456",
                            "https://cdn-images-1.medium.com/max/2000/1*wUcMnW5qarNCTDk7T7918g.jpeg",
                            0,
                            "https://www.zhihu.com/topic/20023724/unanswered?utm_id=0"
                        ),
                        SearchItem(
                            "789",
                            "https://raw.githubusercontent.com/facebook/fresco/main/docs/static/logo.png",
                            0,
                            "https://v.douyin.com/iDSegyfp/"
                        ),
                        SearchItem(
                            "101112",
                            "https://raw.githubusercontent.com/facebook/fresco/main/docs/static/logo.png",
                            0,
                            "https://v.douyin.com/iDSegyfp/"
                        )
                    )
                )
            }
        }
        emit(Result.success(itemList))
    }
}