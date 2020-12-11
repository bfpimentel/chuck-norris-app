package dev.pimentel.chucknorris.testshared

import dev.pimentel.chucknorris.shared.schedulerprovider.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

class TestDispatchersProvider(testDispatcher: TestCoroutineDispatcher) : DispatchersProvider {

    override val ui: CoroutineDispatcher = testDispatcher

    override val io: CoroutineDispatcher = testDispatcher
}
