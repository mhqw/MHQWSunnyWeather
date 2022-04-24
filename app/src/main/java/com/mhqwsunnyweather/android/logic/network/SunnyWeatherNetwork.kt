package com.mhqwsunnyweather.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {

    private val weatherService = ServiceCreator.create<WeatherService>()

    suspend fun getDailyWeather(lng: String,lat: String) =
        weatherService.getDailyWeather(lng,lat).await()

    suspend fun getRealtimeWeather(lng: String,lat: String) =
        weatherService.getRealtimeWeather(lng,lat).await()

    private val placeService = ServiceCreator.create<PlaceService>()

    //获得查询结果 PlaceService
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    //回调简化，利用suspendCoroutine使得回调模板化
    private suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body != null){
                        continuation.resume(body)
                    }else{
                        continuation.resumeWithException(RuntimeException("response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}