package com.trelp.imgur.ui.base

import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import com.trelp.imgur.presentation.FlowRouter
import com.trelp.imgur.di.FlowNav
import com.trelp.imgur.di.IComponent
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

abstract class FlowFragment<T : IComponent> @ContentView constructor(
    @LayoutRes contentLayoutId: Int
) : BaseFragment<T>(contentLayoutId) {

    abstract val container: Int

    @Inject
    @FlowNav
    lateinit var flowRouter: FlowRouter

    @Inject
    @FlowNav
    lateinit var navHolder: NavigatorHolder

    private val navigator: Navigator by lazy {
        object : SupportAppNavigator(requireActivity(), childFragmentManager, container) {}
    }

    override fun onResume() {
        super.onResume()
        navHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navHolder.removeNavigator()
        super.onPause()
    }
}