package com.example.banquemisr.interceptor

import android.annotation.SuppressLint
import com.example.agriwise.ui.activity.SignInActivity
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    @SuppressLint("SuspiciousIndentation")
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = if (SignInActivity.token.isNullOrEmpty()) {
            originalRequest
        } else {
            originalRequest.newBuilder()
                .addHeader("Authorization", "JWT ${SignInActivity.token}")
                .build()
        }

        return chain.proceed(newRequest)
    }
}
