package com.mhqwsunnyweather.android.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.mhqwsunnyweather.android.SunnyWeatherApplication
import com.mhqwsunnyweather.android.logic.model.Place

object PlaceDao {

    fun savePlace(place: Place){
        sharedPreferences().edit {
            putString("place",Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place{
        val placeJson = sharedPreferences().getString("place","")
        return Gson().fromJson(placeJson,Place::class.java)
    }

    fun isSavedPlace() = sharedPreferences().contains("place")

    private fun sharedPreferences() = SunnyWeatherApplication.context
        .getSharedPreferences("sunny_weather",Context.MODE_PRIVATE)

}