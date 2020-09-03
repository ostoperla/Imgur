package com.trelp.imgur.di.activity

import com.trelp.imgur.BuildConfig
import com.trelp.imgur.di.ActivityScope
import com.trelp.imgur.domain.OAuthParams
import dagger.Module
import dagger.Provides

@Module
object AuthModule {

    @Provides
    @ActivityScope
    fun provideOAuthParams() =
        OAuthParams(
            BuildConfig.SERVER_PATH,
            BuildConfig.CLIENT_ID,
            BuildConfig.OAUTH_CALLBACK,
            BuildConfig.RESPONSE_TYPE
        )
}