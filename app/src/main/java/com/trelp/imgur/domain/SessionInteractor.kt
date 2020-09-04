package com.trelp.imgur.domain

import com.trelp.imgur.data.UserRepository
import com.trelp.imgur.di.FragmentScope
import io.reactivex.Completable
import javax.inject.Inject

@FragmentScope
class SessionInteractor @Inject constructor(
    private val userRepo: UserRepository,
    private val oAuthParams: OAuthParams
) {
    val oAuthUrl = oAuthParams.serverPath +
            "oauth2/authorize?client_id=${oAuthParams.clientId}" +
            "&response_type=${oAuthParams.responseType}"

    val currentAccount
        get() = userRepo.currentAccount

    var currentAccountId
        get() = userRepo.currentAccountId
        set(value) {
            userRepo.currentAccountId = value
        }

    val accounts
        get() = userRepo.accounts

    fun checkOAuthRedirect(url: String) =
        url.indexOf(oAuthParams.oAuthCallback) == 0

    fun isUserDidGrandPermission(oAuthRedirect: String) =
        !oAuthRedirect.contains("?$ERROR_ACCESS_DENIED")

    fun login(oAuthRedirect: String): Completable =
        userRepo
            .login(oAuthRedirect)
            .ignoreElement()

    companion object {
        private const val ERROR_ACCESS_DENIED = "error=access_denied"
    }
}