package com.trelp.imgur.presentation

import android.content.Context
import androidx.annotation.StringRes
import com.trelp.imgur.di.AppContext
import javax.inject.Inject

class ResourceManager @Inject constructor(
    @AppContext private val context: Context
) {
    fun getString(@StringRes resId: Int) = context.getString(resId)

    fun getString(@StringRes resId: Int, vararg formatArgs: Any) =
        context.getString(resId, *formatArgs)
}