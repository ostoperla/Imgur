package com.trelp.imgur.di.activity

import com.trelp.imgur.di.ActivityScope
import com.trelp.imgur.di.GlobalNav
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone

@Module
object NavModule {

    private val cicerone = Cicerone.create()

    @Provides
    @ActivityScope
    @GlobalNav
    fun provideNavHolder() = cicerone.navigatorHolder

    @Provides
    @ActivityScope
    @GlobalNav
    fun provideRouter() = cicerone.router
}