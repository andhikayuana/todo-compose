package id.yuana.todo.compose.util

sealed class UiEvent {
    object PopBackStack : UiEvent()
    object ShowDialog : UiEvent()
    data class Navigate(val route: String, val removeBackStack: Boolean = false) : UiEvent()
    data class ShowSnackbar(val message: String, val action: String? = null) : UiEvent()
}
