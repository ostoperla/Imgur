package com.trelp.imgur.di.about

import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.di.IComponent
import com.trelp.imgur.ui.about.AboutFragment
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface AboutComponent : IComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AboutComponent
    }

    fun inject(aboutFragment: AboutFragment)
}