package dev.pimentel.domain

import dev.pimentel.data.dataModules
import dev.pimentel.domain.usecases.GetErrorType
import org.koin.dsl.module

private val useCasesModule = module {
    single { GetErrorType() }
}

val domainModules = listOf(
    useCasesModule
) + dataModules
