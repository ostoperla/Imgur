package com.trelp.imgur.ui.auth

import android.os.Bundle
import com.trelp.imgur.R
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.auth.AuthComponent
import com.trelp.imgur.di.flow.auth.AuthFlowComponent
import com.trelp.imgur.ui.base.BaseFragment
import timber.log.Timber

class AuthFragment : BaseFragment<AuthComponent>(R.layout.fragment_auth) {

    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d(javaClass.simpleName)
    }
    //endregion

    //region Dagger
    override fun setupComponent() {
        Injector.getComponent(this).inject(this)
    }

    override fun getComponentKey() = "Auth"

    override fun createComponent() =
        Injector.findComponent<AuthFlowComponent>().authComponentFactory().create()
    //endregion
}