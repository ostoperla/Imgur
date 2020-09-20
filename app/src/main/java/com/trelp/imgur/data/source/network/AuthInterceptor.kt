package com.trelp.imgur.data.source.network

import com.trelp.imgur.BuildConfig
import com.trelp.imgur.data.UserRepository
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val userRepo: UserRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(
        chain.request().newBuilder().apply {
            userRepo.currentAccount?.let {
                addHeader(AUTHORIZATION, "$BEARER ${it.accessToken}")
            } ?: addHeader(AUTHORIZATION, "$CLIENT_ID ${BuildConfig.CLIENT_ID}")
        }.build()
    )

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer"
        private const val CLIENT_ID = "Client-ID"
    }
}