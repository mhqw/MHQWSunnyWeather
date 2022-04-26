package com.mhqwsunnyweather.android.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.mhqwsunnyweather.android.logic.dao.PlaceDao
import com.mhqwsunnyweather.android.logic.model.Place
import com.mhqwsunnyweather.android.logic.model.PlaceResponse
import com.mhqwsunnyweather.android.logic.model.Weather
import com.mhqwsunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {

    //使用liveData()获得LiveData对象，其代码块中提供挂起函数的上下文，所以可以在代码块中使用任意的挂起函数
    //Dispatchers.IO作为参数，代码块中的代码运行在子线程中
    //因为主线程不允许网络请求
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if(placeResponse.status == "ok"){
            //获得城市数据
            val places = placeResponse.places
            Log.d("ygy","获取地点成功,共${places.size}个地点")
            Result.success(places)
        }else{
            Log.d("ygy","获取地点失败")
            //包装异常信息
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng: String,lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if(realtimeResponse.status == "ok" && dailyResponse.status == "ok"){
                Log.d("ygy","获取天气成功")
                val weather = Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
                Result.success(weather)
            }else{
                Log.d("ygy","获取天气失败," + "realtime response error is ${realtimeResponse.status}, " +
                        "daily response status is ${dailyResponse.status}")
                Result.failure<Weather>(RuntimeException(
                    "realtime response status is ${realtimeResponse.status}" +
                            "daily response status is ${dailyResponse.status}"))
            }
        }
    }

    //这里读取数据的操作不应该在主线程中进行
    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isSavedPlace() = PlaceDao.isSavedPlace()

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            }catch (e: Exception){
                Result.failure<T>(e)
            }
            emit(result)
        }

}