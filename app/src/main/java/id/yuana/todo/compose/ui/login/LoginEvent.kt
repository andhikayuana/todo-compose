package id.yuana.todo.compose.ui.login

sealed class LoginEvent {
    data class OnEmailChange(val email: String): LoginEvent()
    data class OnPasswordChange(val password: String): LoginEvent()
    object OnLoginClick : LoginEvent()
    object OnGotoRegisterClick : LoginEvent()
}
