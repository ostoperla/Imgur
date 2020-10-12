package com.trelp.imgur.presentation

import com.trelp.imgur.Screens
import com.trelp.imgur.di.ActivityScope
import com.trelp.imgur.di.GlobalNav
import com.trelp.imgur.domain.LaunchInteractor
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@ActivityScope
class AppLauncher @Inject constructor(
    @GlobalNav private val router: Router,
    private val launchInteractor: LaunchInteractor
) {

    fun launch() {
        if (launchInteractor.hasAccount) {
            router.newRootScreen(Screens.DrawerFlow)
        } else {
            router.newRootScreen(Screens.AuthFlow)
        }
    }
}