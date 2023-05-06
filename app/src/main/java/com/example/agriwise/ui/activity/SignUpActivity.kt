package com.example.agriwise.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.agriwise.MainActivity
import com.example.agriwise.R
import com.example.agriwise.data.model.RegisterBody
import com.example.agriwise.databinding.ActivitySignUpBinding
import com.example.agriwise.ui.BaseActivity
import com.example.agriwise.ui.viewmodel.Login_Register_ViewModel

class SignUpActivity : BaseActivity() {
    lateinit var binding : ActivitySignUpBinding
    private val viewModel: Login_Register_ViewModel by lazy {
        ViewModelProvider(this).get(Login_Register_ViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeColor()
        binding.signInBtn.setOnClickListener { startActivity(Intent(this, SignInActivity::class.java)) }
        binding.signUpBtn.setOnClickListener {
            val userName = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passEditText.text.toString()
            if (binding.emailEditText.text.isEmpty()) {
                binding.emailEditText.setError("Required Field")
            } else if (binding.passEditText.text.isEmpty()) {
                binding.passEditText.setError("Required Field")
            } else if (binding.usernameEditText.text.isEmpty()) {
                binding.usernameEditText.setError("Required Field")
            }
            else if (!binding.passEditText.text.toString().equals(binding.confirmPassEditText.text.toString(),true)){
                binding.confirmPassEditText.setError("Password Not Match")
            }
            else {

                // Show Progress Bar
                showLoading()

                val register = viewModel.register(RegisterBody(userName, email, password))
                register.observe(this) {

                     if (it=="1"){
                       hideLoading()

                        val builder = AlertDialog.Builder(this)
                        builder
                            .setTitle("Successfully Registered")
                            .setMessage("Going To Login Page")
                            .setPositiveButton("Go") { dialog, which ->
                                startActivity(Intent(this@SignUpActivity,SignInActivity::class.java))
                            }
                            .setCancelable(false)
                            .show()
                    } else {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT)
                            .show()
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun changeColor(){
        val span_text = getString(R.string.have_an_account_login)
        val text = SpannableStringBuilder(span_text)
        val simple = "Login"
        val simple_index = span_text.indexOf(simple)

        text.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this@SignUpActivity,R.color.primary)),simple_index,simple_index+simple.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        binding.signInBtn.setText(text, TextView.BufferType.SPANNABLE)
    }
}