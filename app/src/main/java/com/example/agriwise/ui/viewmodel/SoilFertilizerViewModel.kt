package com.example.agriwise.ui.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agriwise.data.model.CropRecommendationData
import com.example.agriwise.data.model.SoilFertilizerData
import com.example.agriwise.data.model.SoilFertilizerResponse
import com.example.agriwise.data.network.service
import com.example.example.CropRecommendationResponse


import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoilFertilizerViewModel : ViewModel() {

    private val _cropRecommendation = MutableLiveData<CropRecommendationResponse>()
    val cropRecommendation: LiveData<CropRecommendationResponse>
        get() = _cropRecommendation


    private val _soilFertilizer = MutableLiveData<SoilFertilizerResponse>()
    val soilFertilizer: LiveData<SoilFertilizerResponse>
        get() = _soilFertilizer

    init {

    }

    fun getCropRecommendation(cropRecommendationData: CropRecommendationData) {
        viewModelScope.launch {
            service.cropRecommendation(cropRecommendationData).enqueue(object : Callback<CropRecommendationResponse> {
                override fun onResponse(
                    call: Call<CropRecommendationResponse>,
                    response: Response<CropRecommendationResponse>,
                ) {
                    _cropRecommendation.value = response.body()
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
                    _soilFertilizer.value = response.body()
                }

                override fun onFailure(call: Call<SoilFertilizerResponse>, t: Throwable) {
                    Log.e("error", t.localizedMessage as String)
                }
            })
        }

    }


}