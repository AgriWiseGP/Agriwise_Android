package com.example.agriwise.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.WindowCompat
import com.example.agriwise.R
import com.example.agriwise.databinding.ActivityOpeningBinding

class OpeningActivity : AppCompatActivity() {
    lateinit var binding:ActivityOpeningBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpeningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signInBtn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }

    }
}