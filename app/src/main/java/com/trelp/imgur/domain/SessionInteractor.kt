package com.trelp.imgur.domain

import com.trelp.imgur.data.PrefsManager
import com.trelp.imgur.data.SchedulersProvider
import com.trelp.imgur.data.source.network.api.AuthApi
import com.trelp.imgur.domain.session.OAuthParams
import com.trelp.imgur.domain.session.TokenData
import com.trelp.imgur.domain.session.UserAccount
import io.reactivex.Completable
import timber.log.Timber
import java.net.URI
import java.net.URISyntaxException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionInteractor @Inject constructor(
    private val prefs: PrefsManager,
    private val oAuthParams: OAuthParams,
    private val authApi: AuthApi,
    private val schedulers: SchedulersProvider
) {
    val oAuthUrl = oAuthParams.serverPath +
            "oauth2/authorize?client_id=${oAuthParams.clientId}" +
            "&response_type=${oAuthParams.responseType}"

    var currentAccountId
        get() = prefs.currentAccountId
        set(value) {
            prefs.currentAccountId = value
        }

    var accounts
        get() = prefs.accounts
        set(value) {
            prefs.accounts = value
        }

    val currentAccount
        get() = prefs.currentAccount

    /**
     * @return true, if has other accounts
     */
    fun logout(accountId: Int): Boolean {
        with(accounts.toMutableList()) {
            removeAll { it.id == accountId }
            accounts = this
        }

        currentAccountId = 0

        return accounts.firstOrNull() != null
    }

    /**
     * @return true, if has other accounts
     */
    fun logout() = currentAccount?.let { logout(it.id) } ?: false

    fun checkOAuthRedirect(url: String) =
        url.indexOf(oAuthParams.oAuthCallback) == 0

    fun isUserDidGrandPermission(oAuthRedirect: String) =
        !oAuthRedirect.contains("?$ERROR_ACCESS_DENIED")

    fun login(oAuthRedirect: String): Completable {
        return authApi
            .getUserAccount(
                oAuthParams.serverPath,
                oAuthParams.clientId,
                extractTokenDataFromCallbackUrl(oAuthRedirect)
            )
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSuccess(::saveNewAccount)
            .ignoreElement()
    }

    private fun saveNewAccount(account: UserAccount) {
        with(prefs.accounts.toMutableList()) {
            removeAll { it.id == account.id }
            add(account)
            prefs.accounts = this
        }
        prefs.currentAccountId = account.id
    }

    private fun extractTokenDataFromCallbackUrl(oAuthRedirect: String): TokenData {
        try {
            Timber.d(oAuthRedirect)
            val uri = URI(oAuthRedirect)
            val map = linkedMapOf<String, String>()
            val parts = uri.fragment.split("&")
            parts.forEach {
                val values = it.split("=")
                map[values[0]] = if (values.size > 1) values[1] else ""
            }
            var accessToken = ""
            var expiresIn = 0L
            var tokenType = ""
            var refreshToken = ""
            var username = ""
            var id = 0
            map.forEach {
                when (it.key) {
                    ACCESS_TOKEN -> accessToken = it.value
                    EXPIRES_IN -> expiresIn = it.value.toLong()
                    TOKEN_TYPE -> tokenType = it.value
                    REFRESH_TOKEN -> refreshToken = it.value
                    ACCOUNT_USERNAME -> username = it.value
                    ACCOUNT_ID -> id = it.value.toInt()
                }
            }
            return TokenData(
                accessToken,
                id,
                username,
                expiresIn,
                refreshToken,
                scope = "",
                tokenType
            )
        } catch (e: URISyntaxException) {
            throw e
        }
    }

    companion object {
        private const val ERROR_ACCESS_DENIED = "error=access_denied"
        private const val ACCOUNT_USERNAME = "account_username"
        private const val ACCOUNT_ID = "account_id"
        private const val ACCESS_TOKEN = "access_token"
        private const val EXPIRES_IN = "expires_in"
        private const val TOKEN_TYPE = "token_type"
        private const val REFRESH_TOKEN = "refresh_token"
    }
}