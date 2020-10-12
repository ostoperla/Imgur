package com.trelp.imgur.di.app

import com.trelp.imgur.BuildConfig
import com.trelp.imgur.data.source.network.interceptor.AuthInterceptor
import com.trelp.imgur.di.GlideClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
object GlideNetworkModule {

    @Provides
    @Singleton
    @GlideClient
    fun provideGlideOkHttpClient(authInterceptor: AuthInterceptor) =
        OkHttpClient.Builder().apply {
            addNetworkInterceptor(authInterceptor)
            if (BuildConfig.DEBUG) {
                addNetworkInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }
        }.build()
}