package com.trelp.imgur.di.app

import com.trelp.imgur.BuildConfig
import com.trelp.imgur.domain.session.OAuthParams
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AuthModule {

    @Provides
    @Singleton
    fun provideOAuthParams() =
        OAuthParams(
            BuildConfig.SERVER_PATH,
            BuildConfig.CLIENT_ID,
            BuildConfig.OAUTH_CALLBACK,
            BuildConfig.RESPONSE_TYPE
        )
}