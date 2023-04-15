package com.example.agriwise.data.model

import com.google.gson.annotations.SerializedName


data class SoilFertilizerData (

    @SerializedName("crop_name"          ) var cropName          : String?            = null,
    @SerializedName("soil_name"          ) var soilName          : String?            = null,
    @SerializedName("soil_analysis"      ) var soilAnalysis      : SoilAnalysis?      = SoilAnalysis(),
    @SerializedName("weather_conditions" ) var weatherConditions : WeatherConditions? = WeatherConditions()

)