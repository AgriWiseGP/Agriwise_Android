package com.example.agriwise.ui.viewmodel


import android.app.AlertDialog
import android.util.Log
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agriwise.data.model.*
import com.example.agriwise.data.network.service
import com.example.example.CropRecommendationResponse


import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoilFertilizerViewModel : ViewModel() {


    private val _cropRecommendation = MutableLiveData<CropRecommendationResponse?>()
    val cropRecommendation: LiveData<CropRecommendationResponse?>
        get() = _cropRecommendation


    private val _soilFertilizer = MutableLiveData<SoilFertilizerResponse?>()
    val soilFertilizer: LiveData<SoilFertilizerResponse?>
        get() = _soilFertilizer


    private val _soilType = MutableLiveData<SoilTypeResponse?>()
    val soilType: LiveData<SoilTypeResponse?>
        get() = _soilType

    private val _plantDisease = MutableLiveData<SoilTypeResponse?>()
    val plantDisease: LiveData<SoilTypeResponse?>
        get() = _plantDisease

    init {

    }

    fun getPlantDisease(file: MultipartBody.Part) {
        viewModelScope.launch {
            service.plantDisease(Image = file ).enqueue(object : Callback<SoilTypeResponse> {
                override fun onResponse(
                    call: Call<SoilTypeResponse>,
                    response: Response<SoilTypeResponse>,
                ) {
                    if (response.isSuccessful){
                        _plantDisease.value = response.body()
                    }else  {
                        // handle 404 error
                        val errorMessageJson = response.errorBody()?.string()
                        _plantDisease.value = null
                    }
                }

                override fun onFailure(call: Call<SoilTypeResponse>, t: Throwable) {
                    Log.e("error", t.localizedMessage as String)
                }
            })
        }

    }

    fun getSoilType(file: MultipartBody.Part) {
        viewModelScope.launch {
            service.soilType(Image = file ).enqueue(object : Callback<SoilTypeResponse> {
                override fun onResponse(
                    call: Call<SoilTypeResponse>,
                    response: Response<SoilTypeResponse>,
                ) {
                    if (response.isSuccessful){
                        _soilType.value = response.body()
                    }else  {
                        // handle 404 error
                        val errorMessageJson = response.errorBody()?.string()
                        _soilType.value = null
                    }
                }

                override fun onFailure(call: Call<SoilTypeResponse>, t: Throwable) {
                    Log.e("error", t.localizedMessage as String)
                }
            })
        }

    }


    fun getCropRecommendation(cropRecommendationData: CropRecommendationData) {
        viewModelScope.launch {
            service.cropRecommendation(cropRecommendationData).enqueue(object : Callback<CropRecommendationResponse> {
                override fun onResponse(
                    call: Call<CropRecommendationResponse>,
                    response: Response<CropRecommendationResponse>,
                ) {
                    if (response.isSuccessful){
                        _cropRecommendation.value = response.body()
                    }else  {
                        // handle 404 error
                        val errorMessageJson = response.errorBody()?.string()

                        _cropRecommendation.value = null
                    }
                }

                override fun onFailure(call: Call<CropRecommendationResponse>, t: Throwable) {
                    Log.e("error", t.localizedMessage as String)
                }
            })
        }

    }


     fun getSoilFertilizer(soilFertilizerData: SoilFertilizerData) {
        viewModelScope.launch {
            service.soilFertilizer(soilFertilizerData).enqueue(object : Callback<SoilFertilizerResponse> {
                override fun onResponse(
                    call: Call<SoilFertilizerResponse>,
                    response: Response<SoilFertilizerResponse>,
                ) {
                    if (response.isSuccessful){
                        _soilFertilizer.value = response.body()
                    } else {
                        _soilFertilizer.value = null
                    }

                }

                override fun onFailure(call: Call<SoilFertilizerResponse>, t: Throwable) {
                    Log.e("error", t.localizedMessage as String)
                }
            })
        }

    }


}