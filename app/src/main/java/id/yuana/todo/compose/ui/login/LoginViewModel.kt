package id.yuana.todo.compose.ui.login


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.yuana.todo.compose.data.repository.AuthRepository
import id.yuana.todo.compose.util.Routes
import id.yuana.todo.compose.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> {
                email = event.email
            }
            is LoginEvent.OnPasswordChange -> {
                password = event.password
            }
            LoginEvent.OnLoginClick -> {
                viewModelScope.launch {
                    if (email.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Email can't be empty"
                            )
                        )
                        return@launch
                    }

                    if (password.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Password can't be empty"
                            )
                        )
                        return@launch
                    }
                    try {
                        authRepository.signIn(email, password)
                        sendUiEvent(UiEvent.Navigate(
                            route = Routes.TODO_LIST,
                            removeBackStack = true
                        ))
                    } catch (e: Exception) {
                        sendUiEvent(UiEvent.ShowSnackbar(
                            message = e.message ?: "Unknown Error"
                        ))
                    }

                }
            }
            LoginEvent.OnGotoRegisterClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.REGISTER))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}