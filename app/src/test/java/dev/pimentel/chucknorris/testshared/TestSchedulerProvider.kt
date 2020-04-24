package dev.pimentel.chucknorris.testshared

import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

internal class TestSchedulerProvider(
    testScheduler: TestScheduler
) : SchedulerProvider {

    override val ui: Scheduler = testScheduler

    override val io: Scheduler = testScheduler
}
