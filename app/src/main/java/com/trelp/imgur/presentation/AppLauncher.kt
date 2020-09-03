package com.trelp.imgur.presentation

import com.trelp.imgur.Screens
import com.trelp.imgur.di.ActivityScope
import com.trelp.imgur.di.GlobalNav
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@ActivityScope
class AppLauncher @Inject constructor(
    @GlobalNav private val router: Router
) {
    fun launch() {
        Timber.d("Launch Auth Flow")
        router.newRootScreen(Screens.AuthFlow)
    }
}