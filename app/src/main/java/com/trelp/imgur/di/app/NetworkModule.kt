package com.trelp.imgur.di.app

import com.google.gson.Gson
import com.trelp.imgur.BuildConfig
import com.trelp.imgur.data.UserRepository
import com.trelp.imgur.data.source.network.AuthInterceptor
import com.trelp.imgur.data.source.network.ImgurApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(userRepo: UserRepository): OkHttpClient {
        val builder = OkHttpClient.Builder().apply {
            addNetworkInterceptor(AuthInterceptor(userRepo))
            if (BuildConfig.DEBUG) {
                addNetworkInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }
        }
        return builder.build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
        val builder = Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create(gson))
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            client(client)
            baseUrl(BuildConfig.IMGUR_ENDPOINT)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideImgurApi(retrofit: Retrofit) = retrofit.create<ImgurApi>()
}