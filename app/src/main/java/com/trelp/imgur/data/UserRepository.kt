package com.trelp.imgur.data

import com.trelp.imgur.di.ActivityScope
import com.trelp.imgur.domain.UserAccount
import io.reactivex.Single
import java.net.URI
import java.net.URISyntaxException
import javax.inject.Inject

@ActivityScope
class UserRepository @Inject constructor(
    private val prefs: PrefsManager
) {
    val currentAccount
        get() = prefs.accounts.find { it.id == prefs.currentAccountId }

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

    fun login(oAuthRedirect: String): Single<UserAccount> =
        Single.just(extractAccountFromCallbackUrl(oAuthRedirect))
            .doOnSuccess { saveNewAccount(it) }

    private fun saveNewAccount(account: UserAccount) {
        val accounts = prefs.accounts.toMutableList()
        accounts.removeAll { it.id == account.id }
        accounts.add(account)
        prefs.accounts = accounts
        prefs.currentAccountId = account.id
    }

    private fun extractAccountFromCallbackUrl(oAuthRedirect: String) =
        try {
            val uri = URI(oAuthRedirect)
            val map = linkedMapOf<String, String>()
            val parts = uri.fragment.split("&")
            parts.forEach {
                val values = it.split("=")
                map[values[0]] = if (values.size > 1) values[1] else ""
            }
            var username = ""
            var id = 0
            var accessToken = ""
            var expiresIn = 0L
            var tokenType = ""
            var refreshToken = ""
            map.forEach {
                when (it.key) {
                    ACCOUNT_USERNAME -> username = it.value
                    ACCOUNT_ID -> id = it.value.toInt()
                    ACCESS_TOKEN -> accessToken = it.value
                    EXPIRES_IN -> expiresIn = it.value.toLong()
                    TOKEN_TYPE -> tokenType = it.value
                    REFRESH_TOKEN -> refreshToken = it.value
                }
            }
            UserAccount(username, id, accessToken, expiresIn, tokenType, refreshToken, true)
        } catch (e: URISyntaxException) {
            throw e
        }

    companion object {
        private const val ACCOUNT_USERNAME = "account_username"
        private const val ACCOUNT_ID = "account_id"
        private const val ACCESS_TOKEN = "access_token"
        private const val EXPIRES_IN = "expires_in"
        private const val TOKEN_TYPE = "token_type"
        private const val REFRESH_TOKEN = "refresh_token"
    }
}