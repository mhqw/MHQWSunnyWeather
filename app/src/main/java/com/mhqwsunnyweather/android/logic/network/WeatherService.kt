package com.mhqwsunnyweather.android.logic.network


import com.mhqwsunnyweather.android.SunnyWeatherApplication
import com.mhqwsunnyweather.android.logic.model.DailyResponse
import com.mhqwsunnyweather.android.logic.model.PlaceResponse
import com.mhqwsunnyweather.android.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//查询地方
interface WeatherService {

    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") query: String,@Path("lat") lat: String): Call<RealtimeResponse>

    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") query: String,@Path("lat") lat: String): Call<DailyResponse>

}