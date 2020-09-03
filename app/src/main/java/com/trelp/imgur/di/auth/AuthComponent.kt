package com.trelp.imgur.di.auth

import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.di.IComponent
import com.trelp.imgur.ui.auth.AuthFragment
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface AuthComponent : IComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthComponent
    }

    fun inject(authFragment: AuthFragment)
}