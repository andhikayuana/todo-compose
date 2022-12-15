package id.yuana.todo.compose.util

import android.util.Patterns

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)

class EmailValidator {

    /**
     * @param email represent an email address
     */
    fun execute(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The email can't be blank"
            )
        }

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
            return ValidationResult(
                successful = false,
                errorMessage = "That's not a valid email"
            )
        }

        return ValidationResult(successful = true)
    }
}

class PasswordValidator {

    /**
     * @param password represent a password
     */
    fun execute(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consist of at least 8 characters"
            )
        }

        return ValidationResult(successful = true)
    }
}

class PasswordConfirmValidator {

    fun execute(password: String, passwordConfirm: String): ValidationResult {
        if (password != passwordConfirm) {
            return ValidationResult(
                successful = false,
                errorMessage = "The passwords don't match"
            )
        }

        return ValidationResult(successful = true)
    }

}