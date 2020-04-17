package dev.pimentel.domain

import dev.pimentel.data.dataModules
import dev.pimentel.domain.usecases.*
import org.koin.dsl.module

private val useCasesModule = module {
    single { GetErrorType() }
    single { ShuffleList() }
    single { FetchAllCategories(get()) }
    single { FetchAllCategoriesNames(get()) }
    single { SaveAllCategories(get()) }
    single { GetCategorySuggestions(get(), get(), get(), get()) }
}

val domainModules = listOf(
    useCasesModule
) + dataModules
