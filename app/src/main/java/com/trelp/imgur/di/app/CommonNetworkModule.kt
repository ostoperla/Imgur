package com.trelp.imgur.di.app

import com.google.gson.Gson
import com.trelp.imgur.BuildConfig
import com.trelp.imgur.data.PrefsManager
import com.trelp.imgur.data.source.network.TokenHolder
import com.trelp.imgur.data.source.network.interceptor.AuthInterceptor
import com.trelp.imgur.data.source.network.api.ImgurApi
import com.trelp.imgur.data.source.network.interceptor.ErrorResponseInterceptor
import com.trelp.imgur.di.CommonClient
import com.trelp.imgur.di.GlideClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
object CommonNetworkModule {

    @Provides
    @Singleton
    fun povideTokenHolder(prefs: PrefsManager) = TokenHolder(prefs)

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenHolder: TokenHolder) =
        AuthInterceptor(tokenHolder)

    @Provides
    @Singleton
    fun provideErrorResponseInterceptor() = ErrorResponseInterceptor()

    @Provides
    @Singleton
    @CommonClient
    fun provideOkHttpClient(
        @GlideClient okHttpClient: OkHttpClient,
        errorResponseInterceptor: ErrorResponseInterceptor
    ) = okHttpClient.newBuilder().apply {
        addNetworkInterceptor(errorResponseInterceptor)
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, @CommonClient client: OkHttpClient): Retrofit =
        Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create(gson))
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            client(client)
            baseUrl(BuildConfig.IMGUR_ENDPOINT)
        }.build()

    @Provides
    @Singleton
    fun provideImgurApi(retrofit: Retrofit) = retrofit.create<ImgurApi>()
}