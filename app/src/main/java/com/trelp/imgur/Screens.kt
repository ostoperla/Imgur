package com.trelp.imgur

import androidx.fragment.app.Fragment
import com.trelp.imgur.ui.about.AboutFragment
import com.trelp.imgur.ui.auth.AuthFlowFragment
import com.trelp.imgur.ui.auth.AuthFragment
import com.trelp.imgur.ui.bottom.BottomFragment
import com.trelp.imgur.ui.drawer.DrawerFlowFragment
import com.trelp.imgur.ui.drawer.NavDrawerFragment
import com.trelp.imgur.ui.gallery.GalleryFragment
import com.trelp.imgur.ui.profile.ProfileFragment
import com.trelp.imgur.ui.search.SearchFragment
import com.trelp.imgur.ui.settings.SettingsFragment
import com.trelp.imgur.ui.upload.UploadFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {
    //region Auth
    object AuthFlow : SupportAppScreen() {
        override fun getFragment(): Fragment? = AuthFlowFragment()
    }

    object Auth : SupportAppScreen() {
        override fun getFragment(): Fragment? = AuthFragment()
    }
    //endregion

    //region Drawer
    object DrawerFlow : SupportAppScreen() {
        override fun getFragment(): Fragment? = DrawerFlowFragment()
    }

    object NavDrawer : SupportAppScreen() {
        override fun getFragment(): Fragment? = NavDrawerFragment()
    }
    //endregion

    object Bottom : SupportAppScreen() {
        override fun getFragment(): Fragment? = BottomFragment()
    }

    object Gallery : SupportAppScreen() {
        override fun getFragment(): Fragment? = GalleryFragment()
    }

    object Search : SupportAppScreen() {
        override fun getFragment(): Fragment? = SearchFragment()
    }

    object Upload : SupportAppScreen() {
        override fun getFragment(): Fragment? = UploadFragment()
    }

    object Profile : SupportAppScreen() {
        override fun getFragment(): Fragment? = ProfileFragment()
    }

    object Settings : SupportAppScreen() {
        override fun getFragment(): Fragment? = SettingsFragment()
    }

    object About : SupportAppScreen() {
        override fun getFragment(): Fragment? = AboutFragment()
    }
}