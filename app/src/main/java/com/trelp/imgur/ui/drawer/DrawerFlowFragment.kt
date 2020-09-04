package com.trelp.imgur.ui.drawer

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.trelp.imgur.R
import com.trelp.imgur.Screens
import timber.log.Timber

class DrawerFlowFragment : Fragment(R.layout.fragment_drawer_flow) {

    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d(javaClass.simpleName)

        if (childFragmentManager.fragments.isEmpty()) {
            childFragmentManager.commit {
                replace(
                    R.id.navDrawerContainer,
                    Screens.NavDrawer.fragment!!
                )
            }
        }
    }
    //endregion
}