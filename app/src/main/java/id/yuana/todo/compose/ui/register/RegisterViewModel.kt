package id.yuana.todo.compose.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.yuana.todo.compose.data.repository.AuthRepository
import id.yuana.todo.compose.navigation.Screen
import id.yuana.todo.compose.util.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator,
    private val passwordConfirmValidator: PasswordConfirmValidator
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

                registerState = registerState.resetErrorMessages()

                val emailResult = emailValidator.execute(registerState.email)
                val passwordResult = passwordValidator.execute(registerState.password)
                val passwordConfirmResult =
                    passwordConfirmValidator.execute(
                        password = registerState.password,
                        passwordConfirm = registerState.passwordConfirm
                    )

                val hasError = listOf(
                    emailResult,
                    passwordResult,
                    passwordConfirmResult
                ).any { it.successful.not() }

                if (hasError) {
                    registerState = registerState.copy(
                        emailErrorMessage = emailResult.errorMessage,
                        passwordErrorMessage = passwordResult.errorMessage,
                        passwordConfirmErrorMessage = passwordConfirmResult.errorMessage
                    )
                    return
                }

                viewModelScope.launch {
                    try {
                        authRepository.signUp(
                            email = registerState.email.trim(),
                            password = registerState.password.trim()
                        )
                        sendUiEvent(
                            UiEvent.Navigate(
                                route = Screen.TodoList.route,
                                clearBackStack = true
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