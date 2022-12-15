package id.yuana.todo.compose.ui.login


import android.util.Patterns
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

    var loginState by mutableStateOf(LoginState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> {
                loginState = loginState.copy(email = event.email)
            }
            is LoginEvent.OnPasswordChange -> {
                loginState = loginState.copy(password = event.password)
            }
            LoginEvent.OnLoginClick -> {
                viewModelScope.launch {
                    if (loginState.email.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Email can't be empty"
                            )
                        )
                        return@launch
                    }

                    if (Patterns.EMAIL_ADDRESS.matcher(loginState.email).matches().not()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "That's not a valid email"
                            )
                        )
                        return@launch
                    }

                    if (loginState.password.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Password can't be empty"
                            )
                        )
                        return@launch
                    }

                    try {
                        authRepository.signIn(
                            email = loginState.email.trim(),
                            password = loginState.password.trim()
                        )
                        sendUiEvent(
                            UiEvent.Navigate(
                                route = Routes.TODO_LIST,
                                removeBackStack = true
                            )
                        )
                    } catch (e: Exception) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Unknown Error"
                            )
                        )
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