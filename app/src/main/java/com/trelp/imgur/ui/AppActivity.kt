package com.trelp.imgur.ui

import android.os.Bundle
import com.trelp.imgur.R
import com.trelp.imgur.di.HasComponent
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.activity.ActivityComponent
import com.trelp.imgur.di.app.AppComponent
import moxy.MvpAppCompatActivity
import timber.log.Timber

class AppActivity : MvpAppCompatActivity(R.layout.layout_container),
    HasComponent<ActivityComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        Injector.getComponent(this).inject(this)

        super.onCreate(savedInstanceState)

        Timber.d(javaClass.simpleName)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) {
            Injector.destroyComponent(getComponentKey())
        }
    }

    //region Dagger
    override fun getComponentKey() = "AppActivity"

    override fun createComponent() =
        Injector.findComponent<AppComponent>().activityComponentFactory().create()
    //endregion
}