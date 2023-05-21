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
import com.example.agriwise.ui.BaseViewModel
import com.example.agriwise.ui.activity.SignInActivity
import com.example.example.CropRecommendationResponse


import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login_Register_ViewModel : BaseViewModel() {


    private val _cropRecommendation = MutableLiveData<CropRecommendationResponse>()
    val cropRecommendation: LiveData<CropRecommendationResponse>
        get() = _cropRecommendation


    private val _soilFertilizer = MutableLiveData<SoilFertilizerResponse>()
    val soilFertilizer: LiveData<SoilFertilizerResponse>
        get() = _soilFertilizer

    init {

    }



    fun login(loginBody: LoginBody) : LiveData<LoginResponse?>{
        val login = MutableLiveData<LoginResponse?>()
        viewModelScope.launch {
            service.login(loginBody).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful){
                        login.value=response.body()
                        SignInActivity.token = response.body()?.access

                    } else {
                        // handle 404 error
                        val errorMessageJson = response.errorBody()?.string()
                      //  val errorMessage = JSONObject(errorMessageJson).getString("detail")
                        login.value = LoginResponse("", "", errorMessageJson?:"")
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    login.value = LoginResponse("","", t.localizedMessage as String)
                }
            })
        }

        return login
    }



    fun register( registerBody: RegisterBody) : LiveData<String?> {
        val register = MutableLiveData<String?>()
        viewModelScope.launch {
            service.register(registerBody)
                .enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (response.isSuccessful) {
                            register.value="1"
                        } else {
                            register.value = response.errorBody()?.string()


                        }
                    }
                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        register.value = "-1"
                    }
                })
        }

        return register
    }


}