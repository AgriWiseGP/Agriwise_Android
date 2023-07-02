package com.example.agriwise.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.agriwise.R
import com.example.agriwise.data.model.FeaturesData
import com.example.agriwise.data.model.WeatherData
import com.example.agriwise.data.network.WeatherApi
import com.example.agriwise.databinding.DialogLoadingBinding
import com.example.agriwise.databinding.FragmentHomeBinding
import com.example.agriwise.ui.activity.*
import com.example.agriwise.ui.adapter.FeaturesAdapter
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class HomeFragment : Fragment() ,  LocationListener {

private var _binding: FragmentHomeBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!




    private lateinit var locationManager: LocationManager
    private lateinit var weatherApi: WeatherApi
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    var isLoaded = false
    private var loadingDialog: AlertDialog? = null

    companion object {
        const val PERMISSION_REQUEST_LOCATION = 100
        const val WEATHER_API_KEY = "924269d70f4cd9f0b71874f7551eb449"
        const val WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/"
        var temp = ""
        var humiditiy = ""


    }


  @SuppressLint("SuspiciousIndentation")
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

    _binding = FragmentHomeBinding.inflate(inflater, container, false)
      showLoading()
    val root: View = binding.root
      val adapter = FeaturesAdapter {
          when(it){
              0-> startActivity(Intent(requireActivity(),CropSafetyActivity::class.java)) // plant disease
              1-> startActivity(Intent(requireActivity(),CropRecommendationActivity::class.java))
              2-> startActivity(Intent(requireActivity(),SoilFertilizerActivity::class.java))
              3-> startActivity(Intent(requireActivity(),SoilQualityActivity::class.java)) // soil suitability // quality
            //  4-> startActivity(Intent(requireActivity(),SoilFertilizerActivity::class.java)) // crop future
              4-> startActivity(Intent(requireActivity(),SoilClassificationActivity::class.java))
          }

      }
      binding.homeRV.adapter = adapter
        adapter.submitList(listOf(FeaturesData(R.drawable.camera__1_,getString(R.string.my_crop_safety),getString(R.string.crop_safety_content)),
            FeaturesData(R.drawable.sapling__3_,getString(R.string.crops_we_recommend),getString(R.string.recommending_crops)),
            FeaturesData(R.drawable.fertilizer__4_,getString(R.string.soil_fertilizer),getString(R.string.soil_fertilizer_content)),

            FeaturesData(R.drawable.plant__1_,getString(R.string.Soil_suitability_for_cultivation),getString(R.string.soil_suitability_content)),
          //  FeaturesData(R.drawable.pest__1_,getString(R.string.my_crop_future),getString(R.string.crop_future_content)),
            FeaturesData(R.drawable.camera__1_,getString(R.string.soil_classification),getString(R.string.soil_classification_content)),
        //    FeaturesData(R.drawable.greenhouse__4_,getString(R.string.nearest_plantation),getString(R.string.nearest_planting)),
        //    FeaturesData(R.drawable.laboratory__1_,getString(R.string.nearest_laboratory),getString(R.string.nearest_laboratory_content)),
        //    FeaturesData(R.drawable.fertilizer__3_,getString(R.string.purchase_of_fertilizers_and_treatment),getString(R.string.buy_medicine_content))


            ))
      adapter.notifyDataSetChanged()



      val gson = GsonBuilder().create()
      val retrofit = Retrofit.Builder()
          .baseUrl(GetWeatherActivity.WEATHER_API_URL)
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build()
      weatherApi = retrofit.create(WeatherApi::class.java)

      // Check if the user has granted permission for location access
      if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
              GetWeatherActivity.PERMISSION_REQUEST_LOCATION
          )
      } else {
          // Get the user's location
          getLocation()

      }



    return root
  }
    private fun getLocation() {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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
                hideLoading()
                if (response.isSuccessful) {
// Parse the weather data from the response
                    val weatherData = response.body()

                    // Update the UI with the weather data
                    binding.tempDegree.setText((weatherData?.main?.temperature?.minus(273.15)?.roundToInt()?.toDouble()).toString()   +"\u00B0C" )
                    temp= ((weatherData?.main?.temperature?.minus(273.15)?.roundToInt()?.toDouble()).toString()  /* +"\u00B0C" */)
                    humiditiy=(weatherData?.main?.humidity.toString())
                    //  binding.rainfall.setText(weatherData?.rain?.rainfall.toString())
                } else {
                    // Show an error message
                    Toast.makeText(requireContext(), "${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                // Show an error message
                hideLoading()
                Toast.makeText(requireContext(), "${t.message}", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun showLoading() {
        if (loadingDialog == null || loadingDialog?.isShowing == false) {
            hideLoading()
            loadingDialog = createLoadingDialog()
            loadingDialog?.show()
            Log.e(ContentValues.TAG, "showLoading: showing loader")
        } else {
            Log.e(ContentValues.TAG, "showLoading: can't show loader")
        }
    }

    fun hideLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }
    private fun createLoadingDialog(): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())
        val loadingDialogBinding = DialogLoadingBinding.inflate(this.layoutInflater)
        builder.setView(loadingDialogBinding.root)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.window?.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        return dialog
    }

}