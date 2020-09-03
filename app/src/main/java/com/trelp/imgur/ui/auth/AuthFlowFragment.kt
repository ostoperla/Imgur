package com.trelp.imgur.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import timber.log.Timber

class AuthFlowFragment : Fragment() {

    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d(javaClass.simpleName)
    }
    //endregion
}