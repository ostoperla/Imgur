package com.trelp.imgur.presentation

import com.trelp.imgur.Screens
import com.trelp.imgur.data.UserRepository
import com.trelp.imgur.di.ActivityScope
import com.trelp.imgur.di.GlobalNav
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@ActivityScope
class AppLauncher @Inject constructor(
    @GlobalNav private val router: Router,
    private val userRepo: UserRepository
) {
    private val hasAccount
        get() = userRepo.currentAccountId != 0

    fun launch() {
        if (hasAccount) {
            router.newRootScreen(Screens.DrawerFlow)
        } else {
            router.newRootScreen(Screens.AuthFlow)
        }
    }
}