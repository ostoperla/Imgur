package com.trelp.imgur.di.profile

import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.di.IComponent
import com.trelp.imgur.ui.profile.ProfileFragment
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface ProfileComponent : IComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ProfileComponent
    }

    fun inject(profileFragment: ProfileFragment)
}
