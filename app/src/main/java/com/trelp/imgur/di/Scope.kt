package com.trelp.imgur.di

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME

@Scope
@Retention(RUNTIME)
annotation class ActivityScope

@Scope
@Retention(RUNTIME)
annotation class FlowFragmentScope

@Scope
@Retention(RUNTIME)
annotation class FragmentScope