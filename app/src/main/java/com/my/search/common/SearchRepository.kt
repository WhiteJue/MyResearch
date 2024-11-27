package com.my.search.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.my.search.model.SearchItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

object SearchRepository {
    fun search(query: String, count: Int): LiveData<Result<List<SearchItem>>> = liveData(Dispatchers.IO) {
        //子线程中执行
        val result = try {
            //调用API
            val items = SearchNetwork.search(query, count) as List<SearchItem>
            if(items.isNotEmpty()) {
                Result.success(items)
            } else {
                //本地数据兜底
                Result.success(dataMock())
                //Result.failure(RuntimeException("response unexpected"))
            }
        } catch (e: Exception) {
            //本地数据兜底
            Result.success(dataMock())
            //Result.failure<List<SearchItem>>(e)
        }
        //emit方法构建LiveData
        emit(result)
    }

    fun dataMock() : List<SearchItem> {
        return mutableListOf<SearchItem>().also {
            val il = it
            repeat(3) {
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
                            "https://cdn-images-1.medium.com/max/2000/1*wUcMnW5qarNCTDk7T7918g.jpeg",
                            0,
                            "https://v.douyin.com/iDSegyfp/"
                        ),
                        SearchItem(
                            "101112",
                            "https://cdn-images-1.medium.com/max/2000/1*wUcMnW5qarNCTDk7T7918g.jpeg",
                            0,
                            "https://v.douyin.com/iDSegyfp/"
                        )
                    )
                )
            }
        }
    }

    //本地Mock数据（延迟3s，固定12条数据，count参数忽略）
    fun searchMock(query: String, count: Int): LiveData<Result<List<SearchItem>>> = liveData(Dispatchers.IO) {
        delay(1000 * 3)
        emit(Result.success(dataMock()))
    }
}