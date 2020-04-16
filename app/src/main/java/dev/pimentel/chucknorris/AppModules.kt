package dev.pimentel.chucknorris

import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProviderImpl
import dev.pimentel.domain.domainModules
import org.koin.dsl.module

private val schedulerProviderModule = module {
    single<SchedulerProvider> { SchedulerProviderImpl() }
}

val appModules = listOf(
    schedulerProviderModule
) + domainModules
