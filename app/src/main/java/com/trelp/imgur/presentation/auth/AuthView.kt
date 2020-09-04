package com.trelp.imgur.presentation.auth

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AuthView : MvpView {

    fun loadUrl(url: String)

    fun showProgress()

    fun hideProgress()

    fun showError(error: Throwable)

    fun hideError()
}
