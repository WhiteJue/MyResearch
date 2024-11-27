package com.my.search.common

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.*

object SearchNetwork {

    private val searchService = ServiceCreator.create<SearchService>()
    suspend fun search(query: String, count: Int) = searchService.search(query, count).await()

    //扩展方法：从Call<T>获取结果（await），需要从协程调用
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object: Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}

