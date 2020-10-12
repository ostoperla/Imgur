package com.trelp.imgur.data.source.network.interceptor

import com.trelp.imgur.BuildConfig
import com.trelp.imgur.data.source.network.TokenHolder
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenHolder: TokenHolder
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            tokenHolder.accessToken
                ?.let { addHeader(AUTHORIZATION, "$BEARER $it") }
                ?: addHeader(AUTHORIZATION, "$CLIENT_ID ${BuildConfig.CLIENT_ID}")
        }.build()

        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer"
        private const val CLIENT_ID = "Client-ID"
    }
}