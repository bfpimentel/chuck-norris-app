package dev.pimentel.chucknorris.shared.errorhandling

import android.content.Context
import dev.pimentel.chucknorris.R
import dev.pimentel.domain.usecases.shared.UseCase
import java.io.IOException

class GetErrorMessage(
    private val context: Context
) : UseCase<GetErrorMessage.Params, String> {

    override suspend fun invoke(params: Params): String = when (params.throwable) {
        is IOException -> context.getString(R.string.error_message_no_connection)
        else -> context.getString(R.string.error_message_default)
    }

    data class Params(val throwable: Throwable)
}
