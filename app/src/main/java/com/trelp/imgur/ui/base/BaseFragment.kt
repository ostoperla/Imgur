package com.trelp.imgur.ui.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import com.trelp.imgur.di.HasComponent
import com.trelp.imgur.di.IComponent
import com.trelp.imgur.di.Injector
import moxy.MvpAppCompatFragment
import timber.log.Timber

abstract class BaseFragment<T : IComponent> @ContentView constructor(
    @LayoutRes contentLayoutId: Int
) : MvpAppCompatFragment(contentLayoutId), HasComponent<T> {

    private var isSaveState: Boolean = false

    //region LifeCycle
    override fun onAttach(context: Context) {
        super.onAttach(context)

        setupComponent()
    }

    override fun onResume() {
        super.onResume()

        isSaveState = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        isSaveState = true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needDestroyComponent())
            Injector.destroyComponent(getComponentKey())
    }
    //endregion

    open fun onBackPressed() {
        Timber.tag("onBackPressed").d(javaClass.simpleName)
    }

    //region Dagger
    private fun needDestroyComponent() = when {
        requireActivity().isChangingConfigurations -> {
            Injector.log(cause = "isChangeConfig in ${javaClass.simpleName} -> false")
            false
        }
        requireActivity().isFinishing -> {
            Injector.log(cause = "isFinishing in ${javaClass.simpleName} -> true")
            true
        }
        else -> {
            val realRemoving = isRealRemoving()
            Injector.log(cause = "isRealRemoving in ${javaClass.simpleName} -> $realRemoving")
            realRemoving
        }
    }

    // When we rotate device isRemoving() return true for fragment placed in backstack
    // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
    private fun isRealRemoving(): Boolean =
        isRemoving && !isSaveState ||
                ((parentFragment as? BaseFragment<*>)?.isRealRemoving() ?: false)

    abstract fun setupComponent()
    //endregion
}