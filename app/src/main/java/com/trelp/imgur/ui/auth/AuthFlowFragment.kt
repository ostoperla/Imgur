package com.trelp.imgur.ui.auth

import android.os.Bundle
import com.trelp.imgur.R
import com.trelp.imgur.Screens
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.activity.ActivityComponent
import com.trelp.imgur.di.flow.auth.AuthFlowComponent
import com.trelp.imgur.ui.base.BaseFragment
import com.trelp.imgur.ui.base.FlowFragment

class AuthFlowFragment : FlowFragment<AuthFlowComponent>(R.layout.layout_container) {

    override val container: Int
        get() = R.id.fragmentContainer

    private val currentFragment
        get() = childFragmentManager.findFragmentById(R.id.fragmentContainer) as? BaseFragment<*>

    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (childFragmentManager.fragments.isEmpty()) {
            flowRouter.newRootScreen(Screens.Auth)
        }
    }
    //endregion

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }

    //region Dagger
    override fun setupComponent() {
        Injector.getComponent(this).inject(this)
    }

    override fun getComponentKey() = "AuthFlow"

    override fun createComponent() =
        Injector.findComponent<ActivityComponent>().authFlowComponentFactory().create()
    //endregion
}