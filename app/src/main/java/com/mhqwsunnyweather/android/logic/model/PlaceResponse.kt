package com.mhqwsunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

//以下数据类都是根据api网站返回的json结果定义的

data class PlaceResponse(val status: String,val places: List<Place>)

data class Place(val name: String,val location: Location,
    @SerializedName("formatted_address") val address: String)

data class Location(val lng: String,val lat:String)
