package com.trelp.imgur.presentation

import com.trelp.imgur.di.GlobalNav
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen

class FlowRouter(
    @GlobalNav private val globalRouter: Router
) : Router() {

    fun newFlowRootScreen(screen: SupportAppScreen) {
        globalRouter.newRootScreen(screen)
    }

    fun navigateFlowTo(screen: SupportAppScreen) {
        globalRouter.navigateTo(screen)
    }
}