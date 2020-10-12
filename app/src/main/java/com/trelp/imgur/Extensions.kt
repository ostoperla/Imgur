package com.trelp.imgur

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.trelp.imgur.presentation.ResourceManager
import retrofit2.HttpException
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Replace
import java.io.IOException

val ViewGroup.inflater: LayoutInflater
    get() = LayoutInflater.from(context)

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

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


fun Throwable.message(context: Context) = when (this) {
    is HttpException -> {
        when (code()) {
            304 -> context.getString(R.string.not_modified_error)
            400 -> context.getString(R.string.bad_request_error)
            401 -> context.getString(R.string.unauthorized_error)
            403 -> context.getString(R.string.forbidden_error)
            404 -> context.getString(R.string.not_found_error)
            405 -> context.getString(R.string.method_not_allowed_error)
            409 -> context.getString(R.string.conflict_error)
            422 -> context.getString(R.string.unprocessable_error)
            500 -> context.getString(R.string.server_error_error)
            else -> context.getString(R.string.unknown_error)
        }
    }
    is IOException -> context.getString(R.string.network_error)
    else -> context.getString(R.string.unknown_error)
}

fun Throwable.message(resourceManager: ResourceManager) = when (this) {
    is HttpException -> {
        when (code()) {
            304 -> resourceManager.getString(R.string.not_modified_error)
            400 -> resourceManager.getString(R.string.bad_request_error)
            401 -> resourceManager.getString(R.string.unauthorized_error)
            403 -> resourceManager.getString(R.string.forbidden_error)
            404 -> resourceManager.getString(R.string.not_found_error)
            405 -> resourceManager.getString(R.string.method_not_allowed_error)
            409 -> resourceManager.getString(R.string.conflict_error)
            422 -> resourceManager.getString(R.string.unprocessable_error)
            500 -> resourceManager.getString(R.string.server_error_error)
            else -> resourceManager.getString(R.string.unknown_error)
        }
    }
    is IOException -> resourceManager.getString(R.string.network_error)
    else -> resourceManager.getString(R.string.unknown_error)
}