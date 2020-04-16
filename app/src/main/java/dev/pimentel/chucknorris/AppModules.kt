package dev.pimentel.chucknorris

import dev.pimentel.chucknorris.testshared.navigator.Navigator
import dev.pimentel.chucknorris.testshared.navigator.NavigatorImpl
import dev.pimentel.chucknorris.testshared.schedulerprovider.SchedulerProvider
import dev.pimentel.chucknorris.testshared.schedulerprovider.SchedulerProviderImpl
import dev.pimentel.domain.domainModules
import org.koin.dsl.module

private val schedulerProviderModule = module {
    single<SchedulerProvider> { SchedulerProviderImpl() }
}

private val navigatorModule = module {
    single<Navigator> { NavigatorImpl() }
}

val appModules = listOf(
    schedulerProviderModule,
    navigatorModule
) + domainModules
