package com.example.agriwise.data.network

import com.example.agriwise.data.model.WeatherData
import com.example.banquemisr.interceptor.HeaderInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    fun getWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = "924269d70f4cd9f0b71874f7551eb449"
    ): Call<WeatherData>
}



val weatherRetrofit = Retrofit.Builder()
    // .baseUrl("https://coffee-shop2022.herokuapp.com/")
    .baseUrl("https://api.openweathermap.org/data/2.5/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val weatherService = weatherRetrofit.create(WeatherApi::class.java)