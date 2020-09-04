package com.trelp.imgur.di.flow.auth

import com.trelp.imgur.di.FlowFragmentScope
import com.trelp.imgur.di.IComponent
import com.trelp.imgur.di.auth.AuthComponent
import com.trelp.imgur.di.flow.FlowNavModule
import com.trelp.imgur.ui.auth.AuthFlowFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        FlowNavModule::class
    ]
)
@FlowFragmentScope
interface AuthFlowComponent : IComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthFlowComponent
    }

    fun inject(authFlowFragment: AuthFlowFragment)

    fun authComponentFactory(): AuthComponent.Factory
}