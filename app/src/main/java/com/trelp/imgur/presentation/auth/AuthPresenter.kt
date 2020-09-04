package com.trelp.imgur.presentation.auth

import com.trelp.imgur.Screens
import com.trelp.imgur.di.FlowNav
import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.domain.SessionInteractor
import com.trelp.imgur.presentation.FlowRouter
import io.reactivex.disposables.CompositeDisposable
import moxy.MvpPresenter
import javax.inject.Inject

@FragmentScope
class AuthPresenter @Inject constructor(
    @FlowNav private val flowRouter: FlowRouter,
    private val sessionInteractor: SessionInteractor
) : MvpPresenter<AuthView>() {

    private val compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        startAuthorization()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }

    private fun startAuthorization() {
        viewState.loadUrl(sessionInteractor.oAuthUrl)
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
        val loginDisposable = sessionInteractor.login(url)
            .subscribe { flowRouter.newFlowRootScreen(Screens.DrawerFlow) }
        compositeDisposable.add(loginDisposable)
    }

    fun onBackPressed() {
        flowRouter.exit()
    }
}