package com.example.agriwise.ui.activity

import android.Manifest
import android.app.AlertDialog
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
import android.widget.LinearLayout
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
import com.example.agriwise.databinding.DialogLoadingBinding
import com.example.agriwise.ui.BaseActivity
import com.example.agriwise.ui.home.HomeFragment
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

class CropRecommendationActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding : ActivityCropRecommendationBinding
    private lateinit var bottomSheetImageBinding: BottomSheetImageBinding
    lateinit var bottomSheetImageDialog: BottomSheetDialog
    var imageuri: Uri? = null


    private val viewModel: SoilFertilizerViewModel by lazy {
        ViewModelProvider(this).get(SoilFertilizerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCropRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cropSafety = this
        binding.temprature.setText(HomeFragment.temp)
        binding.humidity.setText(HomeFragment.humiditiy)




        binding.backBtn.setOnClickListener { onBackPressed() }

        viewModel.cropRecommendation.observe(this) {
            // response here
            hideLoading()
            if (it?.data !=null){
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                builder
                    .setTitle("We Recommend : ${it.data?.name}")
                    .setPositiveButton("Try another") { dialog, which ->
                        dialog.dismiss()
                        bottomSheetImageDialog.show()
                    }
                    .setNegativeButton("Go Back"){ dialog, which ->
                        dialog.dismiss()
                        finish()
                    }
                    .setCancelable(false)
                    .show()
            } else {
                Toast.makeText(this, "Something went wrong. Please try again later", Toast.LENGTH_LONG).show()
            }
        }

    }



    override fun onClick(v: View?) {
        when (v) {


            binding.recommendButton -> {
                showLoading()
                viewModel.getCropRecommendation( cropRecommendationData = CropRecommendationData(p = binding.pRatio.text.toString().toDouble(), k = binding.kRatio.text.toString().toDouble(), n = binding.nRatio.text.toString().toDouble(), ph = binding.phRatio.text.toString().toDouble(),
                    temperature = binding.temprature.text.toString().toDouble(), humidity = binding.humidity.text.toString().toDouble(), rainfall = binding.rainfall.text.toString().toDouble()
                ))

            }

        }
    }




}