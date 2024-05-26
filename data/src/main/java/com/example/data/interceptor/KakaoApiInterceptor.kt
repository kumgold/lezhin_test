package com.example.data.interceptor

import android.net.TrafficStats
import com.example.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class KakaoApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader("Authorization", BuildConfig.KAKAO_API_KEY)
            .tag(TrafficStats.setThreadStatsTag(Thread.currentThread().id.toInt()))

        return chain.proceed(request.build())
    }
}