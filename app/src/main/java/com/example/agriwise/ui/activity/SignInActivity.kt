package com.example.agriwise.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.agriwise.MainActivity
import com.example.agriwise.R
import com.example.agriwise.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() , View.OnFocusChangeListener {
    lateinit var binding : ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeColor()
        binding.emailEditText.onFocusChangeListener = this
        binding.passEditText.onFocusChangeListener = this
        binding.loginBtn.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }
        binding.backbtn.setOnClickListener { onBackPressed() }
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
}