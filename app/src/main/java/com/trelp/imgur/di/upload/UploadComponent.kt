package com.trelp.imgur.di.upload

import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.di.IComponent
import com.trelp.imgur.ui.upload.UploadFragment
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface UploadComponent : IComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): UploadComponent
    }

    fun inject(uploadFragment: UploadFragment)
}
