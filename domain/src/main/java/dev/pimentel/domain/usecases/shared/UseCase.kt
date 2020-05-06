package dev.pimentel.domain.usecases.shared

internal interface UseCase<Params, Result> {
    operator fun invoke(params: Params): Result
}

object NoParams
