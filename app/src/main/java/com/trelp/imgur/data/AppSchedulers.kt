package com.trelp.imgur.data

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppSchedulers : SchedulersProvider {
    override fun ui() = AndroidSchedulers.mainThread()
    override fun io() = Schedulers.io()
    override fun computation() = Schedulers.computation()
    override fun newThread() = Schedulers.newThread()
    override fun trampoline() = Schedulers.trampoline()
}