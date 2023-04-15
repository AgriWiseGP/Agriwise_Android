package com.example.agriwise.ui.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.agriwise.R
import com.example.agriwise.data.model.WeatherData
import com.example.agriwise.data.network.WeatherApi
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetWeatherActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var weatherApi: WeatherApi
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    companion object {
        const val PERMISSION_REQUEST_LOCATION = 100
        const val WEATHER_API_KEY = "924269d70f4cd9f0b71874f7551eb449"
        const val WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_weather)

        // Initialize Retrofit
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(WEATHER_API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        weatherApi = retrofit.create(WeatherApi::class.java)

        // Check if the user has granted permission for location access
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)
        } else {
            // Get the user's location
            getLocation()
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
    }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude

        // Get the weather data for the user's location
        getWeatherData()
    }

    private fun getWeatherData() {
        weatherApi.getWeatherData(latitude, longitude, WEATHER_API_KEY).enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                if (response.isSuccessful) {
// Parse the weather data from the response
                    val weatherData = response.body()

                    // Update the UI with the weather data
                    val temp = weatherData?.main?.temperature
                    val humidity = weatherData?.main?.temperature
                    val rainFall = weatherData?.rain?.rainfall ?: 0.0

                    Toast.makeText(this@GetWeatherActivity, "${temp}", Toast.LENGTH_SHORT).show()


                } else {
                    // Show an error message
                    Toast.makeText(this@GetWeatherActivity, "${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                // Show an error message
                Toast.makeText(this@GetWeatherActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the user's location
                getLocation()
            } else {
                // Permission denied, show an error message
                Toast.makeText(this@GetWeatherActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onProviderDisabled(provider: String) {
        // Show an error message
        Toast.makeText(this@GetWeatherActivity, "ProviderDisabled", Toast.LENGTH_SHORT).show()
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}
