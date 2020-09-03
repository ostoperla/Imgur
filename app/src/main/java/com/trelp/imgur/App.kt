package com.trelp.imgur

import android.app.Application
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.app.AppComponent
import com.trelp.imgur.di.app.DaggerAppComponent
import timber.log.Timber

class App : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initDagger()
        Injector.init(appComponent)
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.factory()
            .create(this)
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(HyperlinkedDebugTree())
        }
    }
}