package com.trelp.imgur.ui.drawer

import android.os.Bundle
import com.trelp.imgur.R
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.drawer.NavDrawerComponent
import com.trelp.imgur.di.flow.drawer.DrawerFlowComponent
import com.trelp.imgur.ui.base.BaseFragment
import timber.log.Timber

class NavDrawerFragment : BaseFragment<NavDrawerComponent>(R.layout.fragment_nav_drawer) {

    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d(javaClass.simpleName)
    }
    //endregion

    //region Dagger
    override fun setupComponent() {
        Injector.getComponent(this).inject(this)
    }

    override fun getComponentKey() = "NavDrawer"

    override fun createComponent() =
        Injector.findComponent<DrawerFlowComponent>().navDrawerComponentFactory().create()
    //endregion
}