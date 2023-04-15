package com.example.agriwise.ui.activity

import android.content.ContentValues.TAG
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.options
import com.example.agriwise.data.model.SoilAnalysis
import com.example.agriwise.data.model.SoilFertilizerData
import com.example.agriwise.data.model.WeatherConditions
import com.example.agriwise.databinding.ActivityRecommendedCropsBinding
import com.example.agriwise.databinding.BottomSheetImageBinding
import com.example.agriwise.ui.viewmodel.SoilFertilizerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.util.*

class CropSafetyActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding : ActivityRecommendedCropsBinding
    private lateinit var bottomSheetImageBinding: BottomSheetImageBinding
    lateinit var bottomSheetImageDialog: BottomSheetDialog
    var imageuri: Uri? = null



    private val viewModel: SoilFertilizerViewModel by lazy {
        ViewModelProvider(this).get(SoilFertilizerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendedCropsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cropSafety = this
        initBottomSheet()
       binding.backBtn.setOnClickListener { onBackPressed() }

        viewModel.soilFertilizer.observe(this) {
           // response here
            Toast.makeText(this, "Response ${it.target}", Toast.LENGTH_SHORT).show()
        }

    }
    private fun initBottomSheet() {
        bottomSheetImageBinding = BottomSheetImageBinding.inflate(layoutInflater)
        bottomSheetImageDialog = BottomSheetDialog(this)
        bottomSheetImageDialog.setContentView(bottomSheetImageBinding.root)
        bottomSheetImageBinding.openCameraButton.setOnClickListener(this)
        bottomSheetImageBinding.openGalleryButton.setOnClickListener(this)

    }
    private var takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
//            binding.userImageView.setImageURI(imageuri)
            takeCropPicture.launch(
                options(imageuri)
            )
        }
    }

    private var getPicture = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            takeCropPicture.launch(
                options(it)
            )
        }
    }

    private var takeCropPicture = registerForActivityResult(CropImageContract()) {
        if (it.isSuccessful) {
            imageuri = it.uriContent!!
         //   val part = getMultipartForFile(it.uriContent!!)
         /*   profileActivityViewModel.uploadFile(
                part,
                "https://5xw4kug46j.execute-api.us-east-1.amazonaws.com/develop/uploadFile"
            ) */
        }
    }

    override fun onClick(v: View?) {
        when (v) {


            binding.selectImageButton -> {
                viewModel.getSoilFertilizer(SoilFertilizerData(cropName = "Coconut", soilName = "laterite",
                soilAnalysis = SoilAnalysis(Pratio = 4.0, Kratio = 2.1, Nratio = 2.0, PH = 6.98),
                    weatherConditions = WeatherConditions(temperature = 24.01, humidity = 82.06, rainfall = 185.28)
                ))
                bottomSheetImageDialog.show()
            }
            bottomSheetImageBinding.openCameraButton -> {
                val file = File.createTempFile(
                    UUID.randomUUID().toString(),
                    ".jpg",
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                )
                val uri = FileProvider.getUriForFile(this, "com.example.agriwise.fileprovider", file)
                imageuri = uri
                Log.e(TAG, "onClick: $uri")
                takePicture.launch(uri)
                bottomSheetImageDialog.dismiss()
            }
            bottomSheetImageBinding.openGalleryButton -> {
                getPicture.launch("image/*")
                bottomSheetImageDialog.dismiss()
            }
        }
    }
}