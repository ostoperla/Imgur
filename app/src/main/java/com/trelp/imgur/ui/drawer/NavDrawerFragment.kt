package com.trelp.imgur.ui.drawer

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.trelp.imgur.R
import timber.log.Timber

class NavDrawerFragment : Fragment(R.layout.fragment_nav_drawer) {

    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d(javaClass.simpleName)
    }
    //endregion
}