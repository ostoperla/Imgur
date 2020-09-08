package com.trelp.imgur.ui

import android.os.Bundle
import com.trelp.imgur.R
import com.trelp.imgur.di.GlobalNav
import com.trelp.imgur.di.HasComponent
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.activity.ActivityComponent
import com.trelp.imgur.di.app.AppComponent
import com.trelp.imgur.presentation.AppLauncher
import com.trelp.imgur.ui.base.BaseFragment
import moxy.MvpAppCompatActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

class AppActivity : MvpAppCompatActivity(R.layout.layout_container),
    HasComponent<ActivityComponent> {

    @Inject
    lateinit var launcher: AppLauncher

    @Inject
    @GlobalNav
    lateinit var navHolder: NavigatorHolder

    private val navigator: Navigator =
        object : SupportAppNavigator(this, supportFragmentManager, R.id.fragmentContainer) {}

    private val currentFragment
        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as? BaseFragment<*>

    private lateinit var daggerComponentKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        daggerComponentKey = savedInstanceState?.getString(STATE_COMPONENT_KEY)
            ?: "${javaClass.simpleName}#${this.hashCode()}"

        Injector.getComponent(this, daggerComponentKey).inject(this)

        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            launcher.launch()
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()

        navHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navHolder.removeNavigator()

        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(STATE_COMPONENT_KEY, daggerComponentKey)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) {
            Injector.destroyComponent(daggerComponentKey)
        }
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }

    //region Dagger
    override fun createComponent() =
        Injector.findComponent<AppComponent>().activityComponentFactory().create()
    //endregion

    companion object {
        private const val STATE_COMPONENT_KEY = "state_component_key"
    }
}