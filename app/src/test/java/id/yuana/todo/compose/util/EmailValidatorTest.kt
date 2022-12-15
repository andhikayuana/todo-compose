package id.yuana.todo.compose.util

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class EmailValidatorTest {

    private lateinit var emailValidator: EmailValidator

    @Before
    fun setUp() {
        emailValidator = EmailValidator()
    }

    @Test
    fun `given blank email, returns error`() {
        val result = emailValidator.execute("")

        assertEquals(false, result.successful)
    }

    @Test
    fun `given invalid email, returns error`() {
        val result = emailValidator.execute("jarjit")

        assertEquals(false, result.successful)
    }

    @Test
    fun `given valid email, returns success`() {
        val result = emailValidator.execute("jarjit@spam4.me")

        assertEquals(true, result.successful)
    }
}