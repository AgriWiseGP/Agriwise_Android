package com.example.agriwise.ui

import android.app.AlertDialog
import android.content.ContentValues
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.example.agriwise.databinding.DialogLoadingBinding

open class BaseActivity: AppCompatActivity() {
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