package dev.pimentel.domain

import dev.pimentel.data.dataModules
import dev.pimentel.domain.usecases.FetchAllCategories
import dev.pimentel.domain.usecases.FetchAllCategoriesNames
import dev.pimentel.domain.usecases.GetCategorySuggestions
import dev.pimentel.domain.usecases.GetErrorMessage
import dev.pimentel.domain.usecases.SaveAllCategories
import dev.pimentel.domain.usecases.ShuffleList
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val useCasesModule = module {
    single { GetErrorMessage(androidContext()) }
    single { ShuffleList() }
    single { FetchAllCategories(get()) }
    single { FetchAllCategoriesNames(get()) }
    single { SaveAllCategories(get()) }
    single { GetCategorySuggestions(get(), get(), get(), get()) }
}

val domainModules = listOf(
    useCasesModule
) + dataModules
