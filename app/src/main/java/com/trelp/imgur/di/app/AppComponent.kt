package com.trelp.imgur.di.app

import android.content.Context
import com.trelp.imgur.di.AppContext
import com.trelp.imgur.di.IComponent
import com.trelp.imgur.di.activity.ActivityComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        AuthModule::class,
        CommonNetworkModule::class,
        GlideNetworkModule::class
    ]
)
@Singleton
interface AppComponent : IComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance @AppContext context: Context): AppComponent
    }

    fun activityComponentFactory(): ActivityComponent.Factory
}