package com.trelp.imgur.di.drawer

import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.di.IComponent
import com.trelp.imgur.ui.drawer.NavDrawerFragment
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface NavDrawerComponent : IComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): NavDrawerComponent
    }

    fun inject(navDrawerFragment: NavDrawerFragment)
}