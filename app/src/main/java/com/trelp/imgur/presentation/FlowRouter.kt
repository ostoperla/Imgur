package com.trelp.imgur.presentation

import com.trelp.imgur.di.GlobalNav
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen

class FlowRouter(
    @GlobalNav private val globalRouter: Router
) : Router() {

    fun newFlowRootScreen(screen: Screen) {
        globalRouter.newRootScreen(screen)
    }
}