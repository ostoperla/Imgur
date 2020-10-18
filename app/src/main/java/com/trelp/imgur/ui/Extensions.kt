package com.trelp.imgur.ui

import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnAttach

//region WindowInsets (Android 10 API)
fun Window.setColorsForSystemBars(
    @ColorRes statusBarColorRes: Int,
    @ColorRes navigationBarColorRes: Int
) {
    statusBarColor = ContextCompat.getColor(context, statusBarColorRes)
    navigationBarColor = ContextCompat.getColor(context, navigationBarColorRes)
}

/**
 * Use it before inflating layout otherwise it had no effect
 */
fun Window.setEdgeToEdgeSystemUiFlags() {
    decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        decorView.systemUiVisibility = decorView.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
}

fun View.addSystemTopPadding(
    targetView: View = this,
    isConsumed: Boolean = false
) {
    doOnApplyWindowInsets { _, insets, initialPadding ->
        ViewCompat.setPaddingRelative(
            targetView,
            initialPadding.left,
            initialPadding.top + insets.systemWindowInsetTop,
            initialPadding.right,
            initialPadding.bottom
        )
        return@doOnApplyWindowInsets if (isConsumed) {
            insets.replaceSystemWindowInsets(
                Rect(
                    insets.systemWindowInsetLeft,
                    0,
                    insets.systemWindowInsetRight,
                    insets.systemWindowInsetBottom
                )
            )
        } else {
            insets
        }
    }
}

fun View.addSystemBottomPadding(
    targetView: View = this,
    isConsumed: Boolean = false
) {
    doOnApplyWindowInsets { _, insets, initialPadding ->
        ViewCompat.setPaddingRelative(
            targetView,
            initialPadding.left,
            initialPadding.top,
            initialPadding.right,
            initialPadding.bottom + insets.systemWindowInsetBottom
        )
        return@doOnApplyWindowInsets if (isConsumed) {
            insets.replaceSystemWindowInsets(
                Rect(
                    insets.systemWindowInsetLeft,
                    insets.systemWindowInsetTop,
                    insets.systemWindowInsetRight,
                    0
                )
            )
        } else {
            insets
        }
    }
}

/**
 * Если View создается из кода или insets listener устанавливается позже, то система не кинет нам
 * последние значения Insets. Поэтому нужно проверить, что View прикреплена к Window и если это так,
 * нужно запросить у системы Insets, чтобы наш listener их обработал. Если же View создана из кода
 * и еще не прикреплена к верстке, нужно подписаться на это событие и только тогда запросить Insets.
 */
private fun View.doOnApplyWindowInsets(block: OnApplyWindowInsetsListener) {
    val initialPadding = this.initialPadding

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        block(view, insets, initialPadding)
    }

    doOnAttach(ViewCompat::requestApplyInsets)
}

/**
 * Wrapper around [androidx.core.view.OnApplyWindowInsetsListener] which also passes
 * the initial padding set on the view.
 */
private typealias OnApplyWindowInsetsListener = (View, WindowInsetsCompat, Rect) -> WindowInsetsCompat

private inline val View.initialPadding
    get() =
        Rect(
            ViewCompat.getPaddingStart(this),
            paddingTop,
            ViewCompat.getPaddingEnd(this),
            paddingBottom
        )
//endregion