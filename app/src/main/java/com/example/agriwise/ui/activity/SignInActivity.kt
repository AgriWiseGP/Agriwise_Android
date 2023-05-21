package com.example.agriwise.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.agriwise.MainActivity
import com.example.agriwise.R
import com.example.agriwise.data.model.LoginBody
import com.example.agriwise.databinding.ActivitySignInBinding
import com.example.agriwise.ui.BaseActivity
import com.example.agriwise.ui.viewmodel.Login_Register_ViewModel
import com.example.agriwise.ui.viewmodel.SoilFertilizerViewModel

class SignInActivity : BaseActivity() , View.OnFocusChangeListener {
    lateinit var binding : ActivitySignInBinding
    private val viewModel: Login_Register_ViewModel by lazy {
        ViewModelProvider(this).get(Login_Register_ViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeColor()
        binding.emailEditText.onFocusChangeListener = this
        binding.passEditText.onFocusChangeListener = this
        binding.forgotPass.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
        binding.loginBtn.setOnClickListener {

           // startActivity(Intent(this,MainActivity::class.java))

            val email = binding.emailEditText.text.toString()
            val password = binding.passEditText.text.toString()
            if (binding.emailEditText.text.isEmpty()) {
                binding.emailEditText.setError("Required Field")
            } else if (binding.passEditText.text.isEmpty()) {
                binding.passEditText.setError("Required Field")
            } else {
                showLoading()
                val login = viewModel.login(LoginBody(email,password))
                login.observe(this) {
                    if (it?.access.isNullOrEmpty()) {
                       hideLoading()
                        Toast.makeText(this, it?.message, Toast.LENGTH_LONG).show()
                    }
                    else {
                        hideLoading()
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            //startActivity(Intent(this,MainActivity::class.java))
        }
        binding.backbtn.setOnClickListener { onBackPressed() }
        binding.signUpBtn.setOnClickListener { startActivity(Intent(this,SignUpActivity::class.java)) }
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        when (p0) {
            binding.emailEditText -> {
                if (p1) {
                    binding.emailStatus = getString(R.string.active)
                } else {
                    binding.emailStatus = getString(R.string.normal)
                }
            }
            binding.passEditText -> {
                if (p1) {
                    binding.passwordStatus = getString(R.string.active)
                } else {
                    binding.passwordStatus = getString(R.string.normal)
                }
            }

        }
       // binding.error = null
    }
    private fun changeColor(){
        val span_text = getString(R.string.don_t_have_an_account_sign_up)
        val text = SpannableStringBuilder(span_text)
        val simple = "Sign up"
        val simple_index = span_text.indexOf(simple)

        text.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this@SignInActivity,R.color.purple_200)),simple_index,simple_index+simple.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        binding.signUpBtn.setText(text, TextView.BufferType.SPANNABLE)
    }
    companion object{
        var token: String? = null
    }
}