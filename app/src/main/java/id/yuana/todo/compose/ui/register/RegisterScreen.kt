package id.yuana.todo.compose.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import id.yuana.todo.compose.util.UiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    onPopBackStack: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            val focusManager = LocalFocusManager.current

            Text(
                text = "Todo Compose - Register",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = viewModel.registerState.email,
                onValueChange = {
                    viewModel.onEvent(RegisterEvent.OnEmailChange(it))
                },
                placeholder = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email"
                    )
                },
                isError = viewModel.registerState.emailErrorMessage != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            viewModel.registerState.emailErrorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = viewModel.registerState.password,
                onValueChange = {
                    viewModel.onEvent(RegisterEvent.OnPasswordChange(it))
                },
                placeholder = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password"
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                isError = viewModel.registerState.passwordErrorMessage != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            viewModel.registerState.passwordErrorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = viewModel.registerState.passwordConfirm,
                onValueChange = {
                    viewModel.onEvent(RegisterEvent.OnPasswordConfirmChange(it))
                },
                placeholder = { Text(text = "Password Confirm") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password Confirm"
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                isError = viewModel.registerState.passwordConfirmErrorMessage != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            viewModel.registerState.passwordConfirmErrorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    focusManager.clearFocus()
                    viewModel.onEvent(RegisterEvent.OnRegisterClick)
                }
            ) {
                Text(text = "Register")
            }
        }
    }
}