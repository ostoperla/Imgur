package com.trelp.imgur.di.bottom

import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.di.IComponent
import com.trelp.imgur.ui.bottom.BottomFragment
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface BottomComponent : IComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): BottomComponent
    }

    fun inject(bottomFragment: BottomFragment)
}
