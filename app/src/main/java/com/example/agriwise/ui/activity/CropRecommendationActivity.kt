package com.example.agriwise.ui.activity

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.options
import com.example.agriwise.data.model.*
import com.example.agriwise.data.network.WeatherApi
import com.example.agriwise.databinding.ActivityCropRecommendationBinding
import com.example.agriwise.databinding.ActivityRecommendedCropsBinding
import com.example.agriwise.databinding.BottomSheetImageBinding
import com.example.agriwise.ui.viewmodel.SoilFertilizerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import kotlin.math.roundToInt

class CropRecommendationActivity : AppCompatActivity(), View.OnClickListener, LocationListener {
    lateinit var binding : ActivityCropRecommendationBinding
    private lateinit var bottomSheetImageBinding: BottomSheetImageBinding
    lateinit var bottomSheetImageDialog: BottomSheetDialog
    var imageuri: Uri? = null

    private lateinit var locationManager: LocationManager
    private lateinit var weatherApi: WeatherApi
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    var isLoaded = false

    companion object {
        const val PERMISSION_REQUEST_LOCATION = 100
        const val WEATHER_API_KEY = "924269d70f4cd9f0b71874f7551eb449"
        const val WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/"

    }



    private val viewModel: SoilFertilizerViewModel by lazy {
        ViewModelProvider(this).get(SoilFertilizerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCropRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cropSafety = this


        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(GetWeatherActivity.WEATHER_API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        weatherApi = retrofit.create(WeatherApi::class.java)

        // Check if the user has granted permission for location access
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                GetWeatherActivity.PERMISSION_REQUEST_LOCATION
            )
        } else {
            // Get the user's location
            getLocation()
        }


        binding.backBtn.setOnClickListener { onBackPressed() }

        viewModel.cropRecommendation.observe(this) {
            // response here
            if (it.data !=null){
                Toast.makeText(this, "We Recommend : ${it.data?.name}", Toast.LENGTH_LONG).show()
            }
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
        if (!isLoaded) {
            getWeatherData()
            isLoaded = true
        }
    }




    private fun getWeatherData() {
        weatherApi.getWeatherData(latitude, longitude, GetWeatherActivity.WEATHER_API_KEY).enqueue(object :
            Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                if (response.isSuccessful) {
// Parse the weather data from the response
                    val weatherData = response.body()

                    // Update the UI with the weather data
                    binding.temprature.setText((weatherData?.main?.temperature?.minus(273.15)?.roundToInt()?.toDouble()).toString()  /* +"\u00B0C" */)
                    binding.humidity.setText(weatherData?.main?.humidity.toString())

                  //  binding.rainfall.setText(weatherData?.rain?.rainfall.toString())
                } else {
                    // Show an error message
                    Toast.makeText(this@CropRecommendationActivity, "${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                // Show an error message
                Toast.makeText(this@CropRecommendationActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GetWeatherActivity.PERMISSION_REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the user's location
                getLocation()
            } else {
                // Permission denied, show an error message
                Toast.makeText(this@CropRecommendationActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {


            binding.recommendButton -> {
                viewModel.getCropRecommendation( cropRecommendationData = CropRecommendationData(p = binding.pRatio.text.toString().toDouble(), k = binding.kRatio.text.toString().toDouble(), n = binding.nRatio.text.toString().toDouble(), ph = binding.phRatio.text.toString().toDouble(),
                    temperature = binding.temprature.text.toString().toDouble(), humidity = binding.humidity.text.toString().toDouble(), rainfall = binding.rainfall.text.toString().toDouble()
                ))

            }

        }
    }
}