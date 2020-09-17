package com.trelp.imgur.di.flow.drawer

import com.trelp.imgur.di.FlowFragmentScope
import com.trelp.imgur.di.IComponent
import com.trelp.imgur.di.about.AboutComponent
import com.trelp.imgur.di.bottom.BottomComponent
import com.trelp.imgur.di.drawer.NavDrawerComponent
import com.trelp.imgur.di.flow.FlowNavModule
import com.trelp.imgur.di.gallery.GalleryComponent
import com.trelp.imgur.di.profile.ProfileComponent
import com.trelp.imgur.di.search.SearchComponent
import com.trelp.imgur.di.settings.SettingsComponent
import com.trelp.imgur.di.upload.UploadComponent
import com.trelp.imgur.ui.drawer.DrawerFlowFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        FlowNavModule::class
    ]
)
@FlowFragmentScope
interface DrawerFlowComponent : IComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): DrawerFlowComponent
    }

    fun inject(drawerFlowFragment: DrawerFlowFragment)

    fun navDrawerComponentFactory(): NavDrawerComponent.Factory
    fun bottomComponentFactory(): BottomComponent.Factory
    fun settingsComponentFactory(): SettingsComponent.Factory
    fun aboutComponentFactory(): AboutComponent.Factory
    fun galleryComponentFactory(): GalleryComponent.Factory
    fun searchComponentFactory(): SearchComponent.Factory
    fun uploadComponentFactory(): UploadComponent.Factory
    fun profileComponentFactory(): ProfileComponent.Factory
}