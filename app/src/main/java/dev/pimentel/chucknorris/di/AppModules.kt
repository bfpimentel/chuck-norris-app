package dev.pimentel.chucknorris.di

import dev.pimentel.chucknorris.shared.errorhandling.GetErrorMessage
import dev.pimentel.chucknorris.shared.navigator.Navigator
import dev.pimentel.chucknorris.shared.navigator.NavigatorImpl
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.chucknorris.shared.schedulerprovider.SchedulerProviderImpl
import dev.pimentel.data.dataModules
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val schedulerProviderModule = module {
    single<SchedulerProvider> { SchedulerProviderImpl() }
}

private val navigatorModule = module {
    single<Navigator> { NavigatorImpl() }
}

private val errorHandlingModule = module {
    single { GetErrorMessage(androidContext()) }
}

val appModules = listOf(
    schedulerProviderModule,
    navigatorModule,
    errorHandlingModule
) + domainModules + dataModules
