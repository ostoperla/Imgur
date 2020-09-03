package com.trelp.imgur.di.activity

import com.trelp.imgur.di.ActivityScope
import com.trelp.imgur.di.IComponent
import com.trelp.imgur.ui.AppActivity
import dagger.Subcomponent

@Subcomponent(
    modules = [
        AuthModule::class,
        NavModule::class
    ]
)
@ActivityScope
interface ActivityComponent : IComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    fun inject(appActivity: AppActivity)
}