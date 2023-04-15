package com.example.agriwise.data.model

import com.google.gson.annotations.SerializedName


data class WeatherConditions (

    @SerializedName("temperature" ) var temperature : Double? = null,
    @SerializedName("humidity"    ) var humidity    : Double? = null,
    @SerializedName("rainfall"    ) var rainfall    : Double? = null

)