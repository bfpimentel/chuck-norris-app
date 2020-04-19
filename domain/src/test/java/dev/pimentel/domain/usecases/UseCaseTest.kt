package dev.pimentel.domain.usecases

import dev.pimentel.domain.usecases.shared.UseCase
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class UseCaseTest<UseCaseType : UseCase<*, *>> {

    protected abstract val useCase: UseCaseType

    @BeforeEach
    @Test
    fun `should setup subject and it must not be null`() {
        `setup subject`()

        assertNotNull(useCase)
    }

    abstract fun `setup subject`()
}
