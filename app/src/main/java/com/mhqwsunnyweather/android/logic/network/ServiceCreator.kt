package com.mhqwsunnyweather.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {

    private const val BASE_URL = "https://api.caiyunapp.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)  //目标地址
        .addConverterFactory(GsonConverterFactory.create()) //数据转换器
        .build()

    //获得动态代理对象
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    //利用内联函数简化调用操作
    inline fun <reified T> create(): T = create(T::class.java)

}