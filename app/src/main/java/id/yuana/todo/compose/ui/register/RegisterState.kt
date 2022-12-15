package id.yuana.todo.compose.ui.register

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = ""
)
