package com.trelp.imgur.ui.gallery

import android.os.Bundle
import com.trelp.imgur.R
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.flow.drawer.DrawerFlowComponent
import com.trelp.imgur.di.gallery.GalleryComponent
import com.trelp.imgur.ui.base.BaseFragment
import timber.log.Timber

class GalleryFragment : BaseFragment<GalleryComponent>(R.layout.layout_container) {

    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d(javaClass.simpleName)
    }
    //endregion

    //region Dagger
    override fun setupComponent(componentKey: String) {
        Injector.getComponent(this, componentKey).inject(this)
    }

    override fun createComponent() =
        Injector.findComponent<DrawerFlowComponent>().galleryComponentFactory().create()
    //endregion
}