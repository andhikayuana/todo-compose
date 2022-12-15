package id.yuana.todo.compose.ui.register

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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var registerState by mutableStateOf(RegisterState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnEmailChange -> {
                registerState = registerState.copy(email = event.email)
            }
            is RegisterEvent.OnPasswordChange -> {
                registerState = registerState.copy(password = event.password)
            }
            is RegisterEvent.OnPasswordConfirmChange -> {
                registerState = registerState.copy(passwordConfirm = event.passwordConfirm)
            }
            RegisterEvent.OnRegisterClick -> {
                viewModelScope.launch {
                    if (registerState.email.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Email can't be empty"
                            )
                        )
                        return@launch
                    }

                    if (Patterns.EMAIL_ADDRESS.matcher(registerState.email).matches().not()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "That's not a valid email"
                            )
                        )
                        return@launch
                    }

                    if (registerState.password.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Password can't be empty"
                            )
                        )
                        return@launch
                    }

                    if (registerState.password.length < 8) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Password need to consist of at least 8 characters"
                            )
                        )
                        return@launch
                    }

                    if (registerState.passwordConfirm != registerState.password) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "Password Confirm not match"
                            )
                        )
                        return@launch
                    }

                    try {
                        authRepository.signUp(
                            email = registerState.email.trim(),
                            password = registerState.password.trim()
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
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}