package com.trelp.imgur.di.flow

import com.trelp.imgur.di.FlowFragmentScope
import com.trelp.imgur.di.FlowNav
import com.trelp.imgur.di.GlobalNav
import com.trelp.imgur.presentation.FlowRouter
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

@Module
object FlowNavModule {

    @Provides
    @FlowFragmentScope
    @FlowNav
    fun provideCicerone(@GlobalNav router: Router) = Cicerone.create(FlowRouter(router))

    @Provides
    @FlowFragmentScope
    @FlowNav
    fun provideNavHolder(@FlowNav cicerone: Cicerone<FlowRouter>) = cicerone.navigatorHolder

    @Provides
    @FlowFragmentScope
    @FlowNav
    fun provideRouter(@FlowNav cicerone: Cicerone<FlowRouter>) = cicerone.router
}