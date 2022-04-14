package com.mhqwsunnyweather.android.logic

import androidx.lifecycle.liveData
import com.mhqwsunnyweather.android.logic.model.Place
import com.mhqwsunnyweather.android.logic.model.PlaceResponse
import com.mhqwsunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException

object Repository {

    //使用liveData()获得LiveData对象，其代码块中提供挂起函数的上下文，所以可以在代码块中使用任意的挂起函数
    //Dispatchers.IO作为参数，代码块中的代码运行在子线程中
    //因为主线程不允许网络请求
    fun searchPlaces(query: String) = liveData<Result<List<Place>>>(Dispatchers.IO) {
        val result = try{
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if(placeResponse.status == "ok"){
                //获得城市数据
                val places = placeResponse.places
                Result.success(places)
            }else{
                //包装异常信息
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch (e: Exception){
            //包装异常信息
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }

}