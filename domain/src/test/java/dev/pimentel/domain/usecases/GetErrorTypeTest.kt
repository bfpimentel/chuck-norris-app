package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.ErrorType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.IOException

class GetErrorTypeTest {

    private val getErrorType = GetErrorType()

    @Test
    fun `should return no connection error type when throwable is an instance of IOException`() {
        assertEquals(
            ErrorType.NO_CONNECTION,
            getErrorType(GetErrorTypeParams(IOException()))
        )
    }

    @Test
    fun `should return default error type when throwable is not a mapped throwable type`() {
        assertEquals(
            ErrorType.DEFAULT,
            getErrorType(GetErrorTypeParams(IllegalArgumentException()))
        )
    }

    @Test
    fun `GetErrorTypeParams must contain a non null throwable`() {
        val throwable = IllegalArgumentException()

        val params = GetErrorTypeParams(throwable)

        assertNotNull(params.throwable)
        assertEquals(params.throwable, throwable)
    }
}
