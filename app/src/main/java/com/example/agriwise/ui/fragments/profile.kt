package com.example.agriwise.ui.fragments

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.example.agriwise.R
import com.example.agriwise.data.model.User
import com.example.agriwise.databinding.DialogLoadingBinding
import com.example.agriwise.databinding.FragmentDashboardBinding
import com.example.agriwise.databinding.FragmentProfileBinding
import com.example.agriwise.ui.viewmodel.SoilFertilizerViewModel

class profile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var loadingDialog: AlertDialog? = null
    private val viewModel: SoilFertilizerViewModel by lazy {
        ViewModelProvider(this).get(SoilFertilizerViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        showLoading()
        viewModel.getProfile()
        viewModel.userProfile.observe(viewLifecycleOwner){
            if (it!=null){

                binding.userName.text = it.username?:""
                binding.userEmail.setText(it.email?:"")
                binding.userPhone.setText(it.phone_number?:"")
                binding.userDateOfBirth.setText(it.date_of_birth?:"")
            }
            hideLoading()

        }
        binding.updateProfile.setOnClickListener {
            showLoading()
            viewModel.updateProfile(user = User(username = binding.userName.text.toString(), email = binding.userEmail.text.toString(), phone_number = binding.userPhone.text.toString(), date_of_birth = binding.userDateOfBirth.text.toString()))
        }
        return binding.root
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