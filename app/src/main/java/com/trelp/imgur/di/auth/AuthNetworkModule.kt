package com.trelp.imgur.di.auth

import com.trelp.imgur.di.AuthClient
import com.trelp.imgur.di.FragmentScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
object AuthNetworkModule {

    @Provides
    @FragmentScope
    @AuthClient
    fun provideAuthOkHttpClient() = OkHttpClient.Builder().build()
}