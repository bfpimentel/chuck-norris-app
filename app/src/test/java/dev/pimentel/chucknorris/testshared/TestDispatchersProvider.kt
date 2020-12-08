package dev.pimentel.chucknorris.testshared

import dev.pimentel.chucknorris.shared.schedulerprovider.DispatchersProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

internal class TestDispatchersProvider(
    testScheduler: TestScheduler
) : DispatchersProvider {

    override val ui: Scheduler = testScheduler

    override val io: Scheduler = testScheduler
}
