package id.yuana.todo.compose.ui.register

sealed class RegisterEvent {
    data class OnEmailChange(val email: String) : RegisterEvent()
    data class OnPasswordChange(val password: String) : RegisterEvent()
    data class OnPasswordConfirmChange(val passwordConfirm: String) : RegisterEvent()
    object OnRegisterClick: RegisterEvent()
}
