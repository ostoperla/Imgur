package com.trelp.imgur.domain

import com.trelp.imgur.data.PrefsManager
import com.trelp.imgur.di.ActivityScope
import javax.inject.Inject

@ActivityScope
class LaunchInteractor @Inject constructor(
    private val prefs: PrefsManager
) {
    val hasAccount
        get() = prefs.currentAccountId != 0
}