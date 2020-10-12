package com.trelp.imgur.presentation

import android.annotation.SuppressLint
import com.jakewharton.rxrelay2.PublishRelay
import com.trelp.imgur.Screens
import com.trelp.imgur.data.SchedulersProvider
import com.trelp.imgur.data.source.network.exception.ServerError
import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.di.GlobalNav
import com.trelp.imgur.domain.SessionInteractor
import com.trelp.imgur.message
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@FragmentScope
class ErrorHandler @Inject constructor(
    private val sessionInteractor: SessionInteractor,
    private val resourceManager: ResourceManager,
    @GlobalNav private val router: Router,
    private val schedulers: SchedulersProvider
) {
    private val authErrorRelay: PublishRelay<Boolean> = PublishRelay.create()

    init {
        subscribeOnAuthErrors()
    }

    fun proceed(
        error: Throwable,
        messageListener: (String) -> Unit
    ) {
        Timber.e(error)

        when (error) {
            is ServerError -> {

                Timber.e("${error.errorCode}")

                when (error.errorCode) {
                    FORBIDDEN -> authErrorRelay.accept(true)
                    else -> messageListener(error.message(resourceManager))
                }
            }
            else -> messageListener(error.message(resourceManager))
        }
    }

    @SuppressLint("CheckResult")
    private fun subscribeOnAuthErrors() {
        authErrorRelay
            .observeOn(schedulers.ui())
            .subscribe { logout() }
    }

    private fun logout() {
        val hasOtherAccount = sessionInteractor.logout()
        if (hasOtherAccount) {
            router.newRootScreen(Screens.DrawerFlow)
        } else {
            router.newRootScreen(Screens.AuthFlow)
        }
    }

    companion object {
        private const val FORBIDDEN = 403
    }
}