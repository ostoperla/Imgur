package com.trelp.imgur.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationTarget.*

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@Target(FIELD, FUNCTION, VALUE_PARAMETER)
annotation class GlobalNav

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@Target(FIELD, FUNCTION, VALUE_PARAMETER)
annotation class FlowNav