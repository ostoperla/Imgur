package com.trelp.imgur.presentation.gallery

import com.trelp.imgur.presentation.Paginator
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface GalleryView : MvpView {
    fun renderState(state: Paginator.State)
    fun showErrorMessage(msg: String)
}