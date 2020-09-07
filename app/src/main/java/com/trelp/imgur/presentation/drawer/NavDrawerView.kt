package com.trelp.imgur.presentation.drawer

import com.trelp.imgur.domain.UserAccount
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface NavDrawerView : MvpView {

    enum class MenuItem {
        HOME,
        SETTINGS,
        ABOUT,
    }

    fun setAccounts(accounts: List<UserAccount>, currentAccount: UserAccount)

    fun selectMenuItem(item: MenuItem)
}