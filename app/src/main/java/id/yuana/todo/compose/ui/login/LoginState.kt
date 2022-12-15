package id.yuana.todo.compose.ui.login

data class LoginState(
    val email: String = "",
    val emailErrorMessage: String? = null,
    val password: String = "",
    val passwordErrorMessage: String? = null
) {

    fun resetErrorMessages(): LoginState =
        copy(emailErrorMessage = null, passwordErrorMessage = null)
}
