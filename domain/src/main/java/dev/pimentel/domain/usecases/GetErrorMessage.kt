package dev.pimentel.domain.usecases

class GetErrorMessage : UseCase<GetErrorMessageParams, String> {

    @Suppress("ForbiddenComment")
    // TODO: Create an error enum and return it instead of a String
    override fun invoke(params: GetErrorMessageParams): String = ""
}

data class GetErrorMessageParams(val throwable: Throwable)
