package com.trelp.imgur.ui.drawer

import android.os.Bundle
import androidx.fragment.app.commit
import com.trelp.imgur.R
import com.trelp.imgur.Screens
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.activity.ActivityComponent
import com.trelp.imgur.di.flow.drawer.DrawerFlowComponent
import com.trelp.imgur.ui.base.FlowFragment
import timber.log.Timber

class DrawerFlowFragment : FlowFragment<DrawerFlowComponent>(R.layout.fragment_drawer_flow) {

    override val container: Int
        get() = R.id.fragmentContainer

    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d(javaClass.simpleName)

        if (childFragmentManager.fragments.isEmpty()) {
            childFragmentManager.commit {
                replace(
                    R.id.navDrawerContainer,
                    Screens.NavDrawer.fragment!!
                )
            }
        }
    }
    //endregion

    //region Dagger
    override fun setupComponent() {
        Injector.getComponent(this).inject(this)
    }

    override fun getComponentKey() = "DrawerFlow"

    override fun createComponent() =
        Injector.findComponent<ActivityComponent>().drawerFlowComponentFactory().create()
    //endregion
}