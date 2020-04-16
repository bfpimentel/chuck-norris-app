package dev.pimentel.chucknorris.shared.schedulerprovider

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SchedulerProvider {
    val io: Scheduler
    val ui: Scheduler
}

class SchedulerProviderImpl : SchedulerProvider {

    override val io: Scheduler = Schedulers.io()

    override val ui: Scheduler = AndroidSchedulers.mainThread()
}
