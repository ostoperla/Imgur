package com.trelp.imgur.presentation.auth

import com.trelp.imgur.Screens
import com.trelp.imgur.di.FlowNav
import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.domain.SessionInteractor
import com.trelp.imgur.presentation.ErrorHandler
import com.trelp.imgur.presentation.FlowRouter
import com.trelp.imgur.presentation.global.BasePresenter
import javax.inject.Inject

@FragmentScope
class AuthPresenter @Inject constructor(
    @FlowNav private val flowRouter: FlowRouter,
    private val sessionInteractor: SessionInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<AuthView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        startAuthorization()
    }

    private fun startAuthorization() {
        viewState.loadUrl(sessionInteractor.oAuthUrl)
    }

    fun refresh() {
        startAuthorization()
    }

    fun onRedirect(url: String) =
        if (sessionInteractor.checkOAuthRedirect(url)) {
            if (sessionInteractor.isUserDidGrandPermission(url)) {
                requestToken(url)
                true
            } else {
                viewState.loadUrl(url)
                false
            }
        } else {
            false
        }

    private fun requestToken(url: String) {
        sessionInteractor.login(url)
            .doOnSubscribe { viewState.showProgress(true) }
            .doAfterTerminate { viewState.showProgress(false) }
            .subscribe(
                { flowRouter.newFlowRootScreen(Screens.DrawerFlow) },
                { errorHandler.proceed(it) { message -> viewState.showMessage(message) } }
            )
            .connect()
    }

    fun onBackPressed() {
        flowRouter.exit()
    }
}