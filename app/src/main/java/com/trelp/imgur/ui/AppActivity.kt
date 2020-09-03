package com.trelp.imgur.ui

import android.os.Bundle
import com.trelp.imgur.R
import moxy.MvpAppCompatActivity
import timber.log.Timber

class AppActivity : MvpAppCompatActivity(R.layout.layout_container) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d(javaClass.simpleName)
    }
}