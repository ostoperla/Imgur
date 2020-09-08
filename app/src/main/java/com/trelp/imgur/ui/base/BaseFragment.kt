package com.trelp.imgur.ui.base

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
    private lateinit var daggerComponentKey: String

    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        daggerComponentKey = savedInstanceState?.getString(STATE_COMPONENT_KEY)
            ?: "${javaClass.simpleName}#${this.hashCode()}"

        setupComponent(daggerComponentKey)

        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        isSaveState = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        isSaveState = true
        outState.putString(STATE_COMPONENT_KEY, daggerComponentKey)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (needDestroyComponent()) {
            Injector.destroyComponent(daggerComponentKey)
        }
    }
    //endregion

    open fun onBackPressed() {
        Timber.tag("onBackPressed").d(javaClass.simpleName)
    }

    //region Dagger
    private fun needDestroyComponent() = when {
        requireActivity().isChangingConfigurations -> {
            Injector.log(cause = "isChangeConfig in ${javaClass.simpleName}#${this.hashCode()} -> false")
            false
        }
        requireActivity().isFinishing -> {
            Injector.log(cause = "isFinishing in ${javaClass.simpleName}#${this.hashCode()} -> true")
            true
        }
        else -> {
            val realRemoving = isRealRemoving()
            Injector.log(cause = "isRealRemoving in ${javaClass.simpleName}#${this.hashCode()} -> $realRemoving")
            realRemoving
        }
    }

    // When we rotate device isRemoving() return true for fragment placed in backstack
    // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
    private fun isRealRemoving(): Boolean =
        isRemoving && !isSaveState ||
                ((parentFragment as? BaseFragment<*>)?.isRealRemoving() ?: false)

    abstract fun setupComponent(componentKey: String)
    //endregion

    companion object {
        private const val STATE_COMPONENT_KEY = "state_component_key"
    }
}