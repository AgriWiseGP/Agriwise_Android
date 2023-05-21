package com.example.agriwise.ui.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.options
import com.example.agriwise.data.model.SoilAnalysis
import com.example.agriwise.data.model.SoilFertilizerData
import com.example.agriwise.data.model.WeatherConditions
import com.example.agriwise.databinding.ActivityRecommendedCropsBinding
import com.example.agriwise.databinding.ActivitySoilClassificationBinding
import com.example.agriwise.databinding.BottomSheetImageBinding
import com.example.agriwise.ui.BaseActivity
import com.example.agriwise.ui.viewmodel.SoilFertilizerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.util.*

class SoilClassificationActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding : ActivitySoilClassificationBinding
    private lateinit var bottomSheetImageBinding: BottomSheetImageBinding
    lateinit var bottomSheetImageDialog: BottomSheetDialog
    var imageuri: Uri? = null
    var uri: Uri? = null



    private val viewModel: SoilFertilizerViewModel by lazy {
        ViewModelProvider(this).get(SoilFertilizerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoilClassificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cropSafety = this
        initBottomSheet()
        binding.backBtn.setOnClickListener { onBackPressed() }

        viewModel.soilType.observe(this) {
            // response here
            hideLoading()
            if (it!=null){
                createResponseDialog("Success",it.soilType?:"Unable to get result",{bottomSheetImageDialog.show()})
            } else {
                Toast.makeText(this, "Something went wrong. Please try again later", Toast.LENGTH_LONG).show()

            }

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
            /*  takeCropPicture.launch(
                  options(uri)
              ) */

            val part = getMultipartForFile(uri!!)
            showLoading()
            viewModel.getSoilType( file = part)
            // send the multipartBodyPart to your server


        }
    }

    private var getPicture = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            /* takeCropPicture.launch(
                 options(it)
             ) */
            uri = it
            val part = getMultipartForFile(uri!!)
            showLoading()
            viewModel.getSoilType( file = part)
            // send the multipartBodyPart to your server

        }
    }

    private var takeCropPicture = registerForActivityResult(CropImageContract()) {
        if (it.isSuccessful) {
            uri = null
            val part = getMultipartForFile(it.uriContent!!)
            finish()
        }
    }


    override fun onClick(v: View?) {
        when (v) {


            binding.selectImageButton -> {

                bottomSheetImageDialog.show()
            }
            bottomSheetImageBinding.openCameraButton -> {
                val file = File.createTempFile(
                    UUID.randomUUID().toString(),
                    ".jpg",
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                )
                uri = FileProvider.getUriForFile(this, "com.example.agriwise.fileprovider", file)
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