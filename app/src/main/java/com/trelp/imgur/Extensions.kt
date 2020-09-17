package com.trelp.imgur

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Replace

val ViewGroup.inflater: LayoutInflater
    get() = LayoutInflater.from(context)

/**
 * При вызове методов Router, команды попадают в очередь и будут выполнены при установке Navigator.
 * Этот же метод применяет команды в обход очереди.
 */
fun Navigator.newRootScreen(screen: SupportAppScreen) {
    applyCommands(
        arrayOf(
            BackTo(null),
            Replace(screen)
        )
    )
}

val FragmentActivity.fragments: List<Fragment>
    get() = supportFragmentManager.fragments

val Fragment.fragments: List<Fragment>
    get() = childFragmentManager.fragments
