package com.trelp.imgur.data

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun ui(): Scheduler
    fun io(): Scheduler
    fun computation(): Scheduler
    fun newThread(): Scheduler
    fun trampoline(): Scheduler
}