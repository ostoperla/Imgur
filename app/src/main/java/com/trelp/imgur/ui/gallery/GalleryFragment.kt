package com.trelp.imgur.ui.gallery

import android.os.Bundle
import android.view.View
import com.trelp.imgur.R
import com.trelp.imgur.databinding.FragmentGalleryBinding
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.flow.drawer.DrawerFlowComponent
import com.trelp.imgur.di.gallery.GalleryComponent
import com.trelp.imgur.ui.base.BaseFragment

class GalleryFragment : BaseFragment<GalleryComponent>(R.layout.fragment_gallery) {

    private val binding
        get() = viewBinding!! as FragmentGalleryBinding

    //region LifeCycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentGalleryBinding.bind(view)
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