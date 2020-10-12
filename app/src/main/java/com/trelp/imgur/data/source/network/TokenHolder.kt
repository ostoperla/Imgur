package com.trelp.imgur.data.source.network

import com.trelp.imgur.data.PrefsManager
import javax.inject.Inject

class TokenHolder @Inject constructor(
    private val prefs: PrefsManager
) {
    val accessToken
        get() = prefs.currentAccount?.accessToken
}