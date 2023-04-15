package com.example.banquemisr.interceptor

import android.annotation.SuppressLint
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    @SuppressLint("SuspiciousIndentation")
    override fun intercept(chain: Interceptor.Chain): Response {

     val builder =   chain.request().newBuilder()
         .addHeader("Authorization", "JWT " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjgxNDIyMjUyLCJqdGkiOiI0YjZhM2I4ZjhhZjc0NjBkOWUxN2MzMzZhMjJkNmU2NiIsInVzZXJfaWQiOjUzfQ.qMfwgCdXsWLjkhvVVkRtBb0MexHDTmDxTsyD8CcNjtA")
      //   .addHeader("Authorization", "JWT " + "Login.accessToken")
         .build()

        return chain.proceed(builder)
    }
}