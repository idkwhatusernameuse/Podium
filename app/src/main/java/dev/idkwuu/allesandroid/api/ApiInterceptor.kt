package dev.idkwuu.allesandroid.api

import dev.idkwuu.allesandroid.util.SharedPreferences
import okhttp3.*

class ApiInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!request.url.toString().contains("api/login")) {
            request = request.newBuilder()
                .addHeader("authorization", SharedPreferences.login_token!!)
                .build()
        }
        return chain.proceed(request)
    }
}