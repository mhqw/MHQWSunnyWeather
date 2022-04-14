package com.mhqwsunnyweather.android.logic.network

import com.mhqwsunnyweather.android.SunnyWeatherApplication
import com.mhqwsunnyweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//查询地方
interface PlaceService {

    //https://api.caiyun.com/v2/place?query=**&token={token}&lang=zh_CN
    // 除了query地名，其他都是固定值（在GET处写死）
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>

}