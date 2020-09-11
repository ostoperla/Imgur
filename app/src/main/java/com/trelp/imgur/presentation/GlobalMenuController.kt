package com.trelp.imgur.presentation

import com.jakewharton.rxrelay2.PublishRelay
import com.trelp.imgur.di.FlowFragmentScope
import io.reactivex.Observable
import javax.inject.Inject

@FlowFragmentScope
class GlobalMenuController @Inject constructor() {

    private val stateRelay: PublishRelay<Boolean> = PublishRelay.create()
    val state: Observable<Boolean> = stateRelay

    fun open() {
        stateRelay.accept(true)
    }

    fun close() {
        stateRelay.accept(false)
    }
}