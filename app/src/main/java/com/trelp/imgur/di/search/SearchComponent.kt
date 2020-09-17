package com.trelp.imgur.di.search

import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.di.IComponent
import com.trelp.imgur.ui.search.SearchFragment
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface SearchComponent : IComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SearchComponent
    }

    fun inject(searchFragment: SearchFragment)
}
