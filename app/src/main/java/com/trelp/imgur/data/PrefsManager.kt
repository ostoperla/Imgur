package com.trelp.imgur.data

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trelp.imgur.di.AppContext
import com.trelp.imgur.domain.session.UserAccount
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsManager @Inject constructor(
    @AppContext private val context: Context,
    private val gson: Gson
) {
    private val authPrefs by lazy { context.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE) }
    private val accountTypeToken: Type = object : TypeToken<List<UserAccount>>() {}.type

    var currentAccountId
        get() = authPrefs.getInt(KEY_CURRENT_ACCOUNT_ID, 0)
        set(value) = authPrefs.edit { putInt(KEY_CURRENT_ACCOUNT_ID, value) }

    var accounts: List<UserAccount>
        get() = gson.fromJson(authPrefs.getString(KEY_USER_ACCOUNTS, "[]"), accountTypeToken)
        set(value) = authPrefs.edit { putString(KEY_USER_ACCOUNTS, gson.toJson(value)) }

    val currentAccount
        get() = accounts.find { it.id == currentAccountId }

    companion object {
        private const val AUTH_PREFS = "auth_prefs"
        private const val KEY_CURRENT_ACCOUNT_ID = "current_account_id"
        private const val KEY_USER_ACCOUNTS = "user_accounts"
    }
}