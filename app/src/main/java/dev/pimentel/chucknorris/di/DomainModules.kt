package dev.pimentel.chucknorris.di

import dev.pimentel.domain.usecases.AreCategoriesStored
import dev.pimentel.domain.usecases.AreSearchTermsOnLimit
import dev.pimentel.domain.usecases.DeleteLastSearchTerm
import dev.pimentel.domain.usecases.DeleteSearchTerm
import dev.pimentel.domain.usecases.DoesSearchTermExist
import dev.pimentel.domain.usecases.GetAllCategories
import dev.pimentel.domain.usecases.GetAllCategoriesNames
import dev.pimentel.domain.usecases.GetCategorySuggestions
import dev.pimentel.domain.usecases.GetFacts
import dev.pimentel.domain.usecases.GetLastSearchTerms
import dev.pimentel.domain.usecases.GetSearchTerm
import dev.pimentel.domain.usecases.HandleSearchTermSaving
import dev.pimentel.domain.usecases.SaveAllCategories
import dev.pimentel.domain.usecases.SaveAndGetCategoriesSuggestions
import dev.pimentel.domain.usecases.SaveExistingSearchTerm
import dev.pimentel.domain.usecases.SaveNonExistingSearchTerm
import dev.pimentel.domain.usecases.SaveSearchTerm
import org.koin.dsl.module

private val useCasesModule = module {
    single { GetAllCategories(categoriesRepository = get()) }
    single { GetAllCategoriesNames(categoriesRepository = get()) }
    single { SaveAllCategories(categoriesRepository = get()) }
    single { AreCategoriesStored(getAllCategories = get()) }
    single {
        SaveAndGetCategoriesSuggestions(
            getAllCategoriesNames = get(),
            saveAllCategories = get(),
            getCategorySuggestions = get()
        )
    }
    single { GetCategorySuggestions(getAllCategories = get()) }
    single { SaveSearchTerm(searchTermsRepository = get()) }
    single { DeleteSearchTerm(searchTermsRepository = get()) }
    single { DeleteLastSearchTerm(searchTermsRepository = get()) }
    single { AreSearchTermsOnLimit(searchTermsRepository = get()) }
    single { SaveExistingSearchTerm(deleteSearchTerm = get(), saveSearchTerm = get()) }
    single {
        SaveNonExistingSearchTerm(
            areSearchTermsOnLimit = get(),
            deleteLastSearchTerm = get(),
            saveSearchTerm = get()
        )
    }
    single { DoesSearchTermExist(searchTermsRepository = get()) }
    single {
        HandleSearchTermSaving(
            doesSearchTermExist = get(),
            saveExistingSearchTerm = get(),
            saveNonExistingSearchTerm = get()
        )
    }
    single { GetLastSearchTerms(searchTermsRepository = get()) }
    single { GetSearchTerm(searchTermsRepository = get()) }
    single { GetFacts(factsRepository = get()) }
}

val domainModules = listOf(
    useCasesModule
)
