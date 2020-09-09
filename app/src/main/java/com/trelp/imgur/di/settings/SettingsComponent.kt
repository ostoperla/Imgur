package com.trelp.imgur.di.settings

import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.di.IComponent
import com.trelp.imgur.ui.settings.SettingsFragment
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface SettingsComponent : IComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsComponent
    }

    fun inject(settingsFragment: SettingsFragment)
}
