package com.trelp.imgur

import android.view.LayoutInflater
import android.view.ViewGroup
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