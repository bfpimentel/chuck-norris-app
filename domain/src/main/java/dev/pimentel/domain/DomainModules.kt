package dev.pimentel.domain

import dev.pimentel.data.dataModules
import dev.pimentel.domain.usecases.AreSearchTermsOnLimit
import dev.pimentel.domain.usecases.DeleteLastSearchTerm
import dev.pimentel.domain.usecases.DeleteSearchTerm
import dev.pimentel.domain.usecases.DoesSearchTermExist
import dev.pimentel.domain.usecases.GetAllCategories
import dev.pimentel.domain.usecases.GetAllCategoriesNames
import dev.pimentel.domain.usecases.GetCategorySuggestions
import dev.pimentel.domain.usecases.GetErrorMessage
import dev.pimentel.domain.usecases.GetLastSearchTerms
import dev.pimentel.domain.usecases.HandleSearchTermSaving
import dev.pimentel.domain.usecases.SaveAllCategories
import dev.pimentel.domain.usecases.SaveExistingSearchTerm
import dev.pimentel.domain.usecases.SaveNonExistingSearchTerm
import dev.pimentel.domain.usecases.SaveSearchTerm
import dev.pimentel.domain.usecases.ShuffleList
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val useCasesModule = module {
    single { GetErrorMessage(androidContext()) }
    single { ShuffleList() }
    single { GetAllCategories(get()) }
    single { GetAllCategoriesNames(get()) }
    single { SaveAllCategories(get()) }
    single { GetCategorySuggestions(get(), get(), get(), get()) }
    single { SaveSearchTerm(get()) }
    single { DeleteSearchTerm(get()) }
    single { DeleteLastSearchTerm(get()) }
    single { AreSearchTermsOnLimit(get()) }
    single { SaveExistingSearchTerm(get(), get()) }
    single { SaveNonExistingSearchTerm(get(), get(), get()) }
    single { DoesSearchTermExist(get()) }
    single { HandleSearchTermSaving(get(), get(), get()) }
    single { GetLastSearchTerms(get()) }
}

val domainModules = listOf(
    useCasesModule
) + dataModules
