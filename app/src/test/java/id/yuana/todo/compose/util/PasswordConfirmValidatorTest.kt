package id.yuana.todo.compose.util

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class PasswordConfirmValidatorTest {

    private lateinit var passwordConfirmValidator: PasswordConfirmValidator

    @Before
    fun setUp() {
        passwordConfirmValidator = PasswordConfirmValidator()
    }

    @Test
    fun `given invalid password confirm, returns error`() {
        val result = passwordConfirmValidator.execute("password", "invalid")

        assertEquals(false, result.successful)
    }

    @Test
    fun `given valid passwod confirm, returns success`() {
        val result = passwordConfirmValidator.execute("password", "password")

        assertEquals(true, result.successful)
    }
}