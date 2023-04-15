package com.example.agriwise.data.model

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("main") val main: MainData,
    @SerializedName("rain") val rain: RainData?
)

data class MainData(
    @SerializedName("temp") val temperature: Double,
    @SerializedName("humidity") val humidity: Double
)

data class RainData(
    @SerializedName("3h") val rainfall: Double
)
