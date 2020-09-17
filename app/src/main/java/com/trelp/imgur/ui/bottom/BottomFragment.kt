package com.trelp.imgur.ui.bottom

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.commitNow
import com.trelp.imgur.R
import com.trelp.imgur.Screens
import com.trelp.imgur.databinding.FragmentBottomBinding
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.bottom.BottomComponent
import com.trelp.imgur.di.flow.drawer.DrawerFlowComponent
import com.trelp.imgur.fragments
import com.trelp.imgur.simpleName
import com.trelp.imgur.ui.base.BaseFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen
import timber.log.Timber

class BottomFragment : BaseFragment<BottomComponent>(R.layout.fragment_bottom) {

    private val binding
        get() = viewBinding!! as FragmentBottomBinding

    private val currentTabFragment
        get() = fragments.find { !it.isHidden } as? BaseFragment<*>

    //region LifeCycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentBottomBinding.bind(view)

        with(binding.bottomNav) {
            setOnNavigationItemSelectedListener {
                selectTab(
                    when (it.itemId) {
                        GALLERY -> GALLERY_SCREEN
                        SEARCH -> SEARCH_SCREEN
                        UPLOAD -> UPLOAD_SCREEN
                        PROFILE -> PROFILE_SCREEN
                        else -> GALLERY_SCREEN
                    }
                )
                return@setOnNavigationItemSelectedListener true
            }

            Timber.d("fragments: %s", fragments.joinToString { it.simpleName() })
            Timber.d("current fragment: %s", currentTabFragment?.simpleName())

            selectedItemId = when (currentTabFragment?.tag) {
                GALLERY_SCREEN.screenKey -> GALLERY
                SEARCH_SCREEN.screenKey -> SEARCH
                UPLOAD_SCREEN.screenKey -> UPLOAD
                PROFILE_SCREEN.screenKey -> PROFILE
                else -> GALLERY
            }
        }
    }
    //endregion

    private fun selectTab(screen: SupportAppScreen) {
        val tag = screen.screenKey
        val newFragment = childFragmentManager.findFragmentByTag(tag)

        if (newFragment != null && currentTabFragment != null && newFragment == currentTabFragment) {
            Timber.d("same fragments")
            return
        }
        childFragmentManager.commitNow {
            newFragment?.let {
                Timber.d("show new: ${it.simpleName()}")
                show(it)
            } ?: run {
                Timber.d("create new: ${screen.fragment!!.simpleName()}")
                add(R.id.fragmentTabContainer, screen.fragment!!, tag)
            }
            currentTabFragment?.let {
                Timber.d("hide current: ${it.simpleName()}")
                hide(it)
            }
        }

        Timber.d("fragments: %s", fragments.joinToString { it.simpleName() })
    }

    override fun onBackPressed() {
        currentTabFragment?.onBackPressed()
    }

    //region Dagger
    override fun setupComponent(componentKey: String) {
        Injector.getComponent(this, componentKey).inject(this)
    }

    override fun createComponent() =
        Injector.findComponent<DrawerFlowComponent>().bottomComponentFactory().create()
    //endregion

    companion object {
        @IdRes
        private const val GALLERY = R.id.Gallery

        @IdRes
        private const val SEARCH = R.id.Search

        @IdRes
        private const val UPLOAD = R.id.Upload

        @IdRes
        private const val PROFILE = R.id.User

        private val GALLERY_SCREEN = Screens.Gallery
        private val SEARCH_SCREEN = Screens.Search
        private val UPLOAD_SCREEN = Screens.Upload
        private val PROFILE_SCREEN = Screens.Profile
    }
}