package com.trelp.imgur.ui.drawer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.trelp.imgur.R
import com.trelp.imgur.Screens
import com.trelp.imgur.databinding.FragmentDrawerFlowBinding
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.activity.ActivityComponent
import com.trelp.imgur.di.flow.drawer.DrawerFlowComponent
import com.trelp.imgur.ui.base.BaseFragment
import com.trelp.imgur.ui.base.FlowFragment
import timber.log.Timber

class DrawerFlowFragment : FlowFragment<DrawerFlowComponent>(R.layout.fragment_drawer_flow) {

    override val container: Int
        get() = R.id.fragmentContainer

    private var fragmentDrawerFlowBinding: FragmentDrawerFlowBinding? = null
    private val binding get() = fragmentDrawerFlowBinding!!

    private val currentFragment
        get() = childFragmentManager.findFragmentById(container) as? BaseFragment<*>

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
            flowRouter.newRootScreen(Screens.Bottom)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentDrawerFlowBinding = FragmentDrawerFlowBinding.bind(view)
    }

    override fun onDestroyView() {
        fragmentDrawerFlowBinding = null

        super.onDestroyView()
    }
    //endregion

    override fun onBackPressed() {
        with(binding.drawerLayout) {
            if (isOpen) {
                close()
            } else {
                currentFragment?.onBackPressed() ?: flowRouter.exit()
            }
        }
    }

    //region Dagger
    override fun setupComponent(componentKey: String) {
        Injector.getComponent(this, componentKey).inject(this)
    }

    override fun createComponent() =
        Injector.findComponent<ActivityComponent>().drawerFlowComponentFactory().create()
    //endregion
}