package dev.pimentel.chucknorris.shared.schedulerprovider

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersProvider {
    val io: CoroutineDispatcher
    val ui: CoroutineDispatcher
}

class DispatchersProviderImpl : DispatchersProvider {
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val ui: CoroutineDispatcher = Dispatchers.Main
}
