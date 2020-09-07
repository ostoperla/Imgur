package com.trelp.imgur.presentation.drawer

import com.trelp.imgur.Screens
import com.trelp.imgur.di.FlowNav
import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.domain.SessionInteractor
import com.trelp.imgur.domain.UserAccount
import com.trelp.imgur.presentation.FlowRouter
import com.trelp.imgur.presentation.drawer.NavDrawerView.MenuItem
import com.trelp.imgur.presentation.drawer.NavDrawerView.MenuItem.*
import moxy.MvpPresenter
import timber.log.Timber
import javax.inject.Inject

@FragmentScope
class NavDrawerPresenter @Inject constructor(
    @FlowNav private val flowRouter: FlowRouter,
    private val sessionInteractor: SessionInteractor
) : MvpPresenter<NavDrawerView>() {

    private var currentAccount: UserAccount? = null
    private var currentMenuItem: MenuItem? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        sessionInteractor.currentAccount?.let {
            currentAccount = it
            viewState.setAccounts(sessionInteractor.accounts, it)
        }
    }

    fun onProfileClicked() {
        flowRouter.newRootScreen(Screens.Bottom)    // TODO: 06.09.2020 Сразу отобразить ProfileFragment в нем
    }

    fun onLogoutClicked() {
        currentAccount?.let {
            val hasOtherAccount = sessionInteractor.logout(it.id)
            if (hasOtherAccount) {
                flowRouter.newFlowRootScreen(Screens.DrawerFlow)
            } else {
                flowRouter.newFlowRootScreen(Screens.AuthFlow)
            }
        }
    }

    fun onAddAccountClicked() {
        flowRouter.navigateFlowTo(Screens.AuthFlow)
    }

    fun onAccountClicked(account: UserAccount) {
        if (currentAccount != account) {
            sessionInteractor.currentAccountId = account.id
            flowRouter.newFlowRootScreen(Screens.DrawerFlow)
        }
    }

    fun onMenuItemClicked(item: MenuItem) {
        if (item != currentMenuItem) {
            Timber.d("item $item != currentMenuItem $currentMenuItem ${item != currentMenuItem}")
            when (item) {
                HOME -> flowRouter.newRootScreen(Screens.Bottom)
                SETTINGS -> flowRouter.newRootScreen(Screens.Settings)
                ABOUT -> flowRouter.newRootScreen(Screens.About)
            }
        }
    }

    fun onScreenChanged(item: MenuItem) {
        currentMenuItem = item
        Timber.d("$currentMenuItem")
        viewState.selectMenuItem(item)
    }
}