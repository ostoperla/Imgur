package com.trelp.imgur.di.gallery

import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.di.IComponent
import com.trelp.imgur.ui.gallery.GalleryFragment
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface GalleryComponent : IComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): GalleryComponent
    }

    fun inject(galleryFragment: GalleryFragment)
}
