package id.yuana.todo.compose.util

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class PasswordValidatorTest {

    private lateinit var passwordValidator: PasswordValidator

    @Before
    fun setUp() {
        passwordValidator = PasswordValidator()
    }

    @Test
    fun `given blank password, returns error`() {
        val result = passwordValidator.execute("")

        assertEquals(false, result.successful)
    }

    @Test
    fun `given 7 chars password, returns error`() {
        val result = passwordValidator.execute("1234567")

        assertEquals(false, result.successful)
    }

    @Test
    fun `given 8 chars password, returns success`() {
        val result = passwordValidator.execute("password")

        assertEquals(true, result.successful)
    }
}