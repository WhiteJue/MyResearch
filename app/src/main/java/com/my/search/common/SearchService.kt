package com.my.search.common

import com.my.search.model.SearchItemResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

//用于Retrofit构建动态代理
interface SearchService {
    fun search(@Query("query") query: String) : Call<SearchItemResponse>
}


object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"
    //使用JSON
    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //简化Service获取的方法
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}