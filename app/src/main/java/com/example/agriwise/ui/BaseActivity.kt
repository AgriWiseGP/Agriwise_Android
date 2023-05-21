package com.example.agriwise.ui

import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.example.agriwise.databinding.DialogLoadingBinding
import com.example.agriwise.databinding.DialogStatusBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

open class BaseActivity: AppCompatActivity() {
    lateinit var dialogStatusBinding: DialogStatusBinding
    private lateinit var statusAlertDialog: AlertDialog

    fun createResponseDialog(title:String,resultMsg:String,apiCallback: (() -> Unit)?){
        val builder = AlertDialog.Builder(this)
        dialogStatusBinding = DialogStatusBinding.inflate(layoutInflater)
        builder.setCancelable(false)
        dialogStatusBinding.titleTextView.text = title
        dialogStatusBinding.messageTextView.text =resultMsg
        dialogStatusBinding.closeButton.setOnClickListener {
            statusAlertDialog.dismiss()
            finish()

        }

        dialogStatusBinding.tryAgain.setOnClickListener {
            statusAlertDialog.dismiss()
            apiCallback?.invoke()


        }

        builder.setView(dialogStatusBinding.root)
        statusAlertDialog = builder.create()
        statusAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        statusAlertDialog.show()
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

    fun getMultipartForFile(uri: Uri): MultipartBody.Part {
        Log.e(TAG, "onClick: $uri")
       // val mimeType = uri.getMimeType()
       // Log.e(TAG, "onClick: $mimeType")
        Log.e(TAG, "onClick: ${getDisplayName(uri).toString()}")
        val file = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            File(
                cacheDir, getDisplayName(uri).toString()
            )
        } else {
            File(uri.path.toString())
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentResolver.openInputStream(uri).use { `in` ->
                file.outputStream().use {
                    `in`?.copyTo(it)
                }
            }
        }
        Log.e(TAG, "onClick: ${file.length()}")
        val requestBody = file.asRequestBody(
            "multipart/form-data".toMediaTypeOrNull()
        )
        return MultipartBody.Part.createFormData("image", getDisplayName(uri), requestBody)
    }

    fun getDisplayName(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        contentResolver.query(uri, projection, null, null, null).use { cursor ->
            if (cursor != null) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex)
                }
            }
        }
        Log.w(
            "BaseActivity",
            "Couldnt determine DISPLAY_NAME for Uri.  Falling back to Uri path: " + uri.path
        )

        return uri.path.toString()
            .substring(uri.path.toString().lastIndexOf('/') + 1, uri.path.toString().length)
    }

    private var loadingDialog: AlertDialog? = null

    private fun createLoadingDialog(): AlertDialog {
        val builder = AlertDialog.Builder(this)
        val loadingDialogBinding = DialogLoadingBinding.inflate(this.layoutInflater)
        builder.setView(loadingDialogBinding.root)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.window?.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        return dialog
    }
}