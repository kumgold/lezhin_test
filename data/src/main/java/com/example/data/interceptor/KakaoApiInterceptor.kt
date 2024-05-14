package com.example.data.interceptor

import com.example.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class KakaoApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader("Authorization", BuildConfig.KAKAO_API_KEY)

        return chain.proceed(request.build())
    }
}