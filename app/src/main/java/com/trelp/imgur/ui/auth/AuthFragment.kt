package com.trelp.imgur.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.trelp.imgur.R
import timber.log.Timber

class AuthFragment : Fragment(R.layout.fragment_auth) {

    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d(javaClass.simpleName)
    }
    //endregion
}