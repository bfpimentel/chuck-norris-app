package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.ErrorType
import dev.pimentel.domain.usecases.shared.UseCase
import java.io.IOException

class GetErrorType :
    UseCase<GetErrorTypeParams, ErrorType> {

    override fun invoke(params: GetErrorTypeParams): ErrorType = when (params.throwable) {
        is IOException -> ErrorType.NO_CONNECTION
        else -> ErrorType.DEFAULT
    }
}

data class GetErrorTypeParams(val throwable: Throwable)
