package com.trelp.imgur

import timber.log.Timber

class HyperlinkedDebugTree(
    private val showMethodName: Boolean = true
) : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? =
        with(element) {
            "($fileName:$lineNumber) ${if (showMethodName) "$methodName()" else ""}"
        }
}

inline fun Any?.log(prefix: String = "object:") = Timber.d("$prefix${toString()}")