package dev.idkwuu.allesandroid.api

import dev.idkwuu.allesandroid.util.SharedPreferences
import okhttp3.*

class ApiInterceptor: Interceptor, Authenticator {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .build()
        return chain.proceed(request)
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        var requestAvailable: Request? = null
        try {
            requestAvailable = response.request.newBuilder()
                .build()
            return requestAvailable
        } catch (ex: Exception) { }
        return requestAvailable
    }
}