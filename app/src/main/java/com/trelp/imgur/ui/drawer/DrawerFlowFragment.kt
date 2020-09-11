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
import com.trelp.imgur.newRootScreen
import com.trelp.imgur.presentation.GlobalMenuController
import com.trelp.imgur.presentation.drawer.NavDrawerView.MenuItem.*
import com.trelp.imgur.ui.about.AboutFragment
import com.trelp.imgur.ui.base.BaseFragment
import com.trelp.imgur.ui.base.FlowFragment
import com.trelp.imgur.ui.bottom.BottomFragment
import com.trelp.imgur.ui.settings.SettingsFragment
import io.reactivex.disposables.Disposable
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import timber.log.Timber
import javax.inject.Inject

class DrawerFlowFragment : FlowFragment<DrawerFlowComponent>(R.layout.fragment_drawer_flow) {

    override val container: Int
        get() = R.id.fragmentContainer

    private val binding
        get() = viewBinding!! as FragmentDrawerFlowBinding

    private val currentFragment
        get() = childFragmentManager.findFragmentById(container) as? BaseFragment<*>

    private val navDrawerFragment
        get() = childFragmentManager.findFragmentById(R.id.navDrawerContainer) as? NavDrawerFragment

    override val navigator: Navigator by lazy {
        object : SupportAppNavigator(requireActivity(), childFragmentManager, container) {

            override fun applyCommands(commands: Array<out Command>) {
                super.applyCommands(commands)

                Timber.d(
                    "commands = ${
                        commands.toList().joinToString(prefix = "(", postfix = ")") {
                            it.javaClass.simpleName
                        }
                    }")

                updateNavDrawer()
            }
        }
    }

    @Inject
    lateinit var menuController: GlobalMenuController

    private var menuStateDisposable: Disposable? = null

    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (childFragmentManager.fragments.isEmpty()) {
            childFragmentManager.commit {
                replace(
                    R.id.navDrawerContainer,
                    Screens.NavDrawer.fragment!!
                )
            }
            navigator.newRootScreen(Screens.Bottom)
        } else {
            updateNavDrawer()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentDrawerFlowBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()

        menuStateDisposable = menuController.state.subscribe(::openNavDrawer)
    }

    override fun onPause() {
        menuStateDisposable?.dispose()

        super.onPause()
    }
    //endregion

    //region NavDrawer
    private fun updateNavDrawer() {
        childFragmentManager.executePendingTransactions()

        navDrawerFragment?.let { navDrFrag ->
            currentFragment?.let {
                when (it) {
                    is BottomFragment -> navDrFrag.onScreenChanged(HOME)
                    is SettingsFragment -> navDrFrag.onScreenChanged(SETTINGS)
                    is AboutFragment -> navDrFrag.onScreenChanged(ABOUT)
                }
            }
        }
    }

    private fun openNavDrawer(open: Boolean) {
        with(binding.drawerLayout) {
            if (open) open() else close()
        }
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