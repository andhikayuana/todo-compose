package id.yuana.todo.compose.ui.login

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import id.yuana.todo.compose.util.UiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action
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
                text = "Todo Compose - Login",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = viewModel.loginState.email,
                onValueChange = {
                    viewModel.onEvent(LoginEvent.OnEmailChange(it))
                },
                placeholder = {
                    Text(text = "Email")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email"
                    )
                },
                isError = viewModel.loginState.emailErrorMessage != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )
            viewModel.loginState.emailErrorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = viewModel.loginState.password,
                onValueChange = {
                    viewModel.onEvent(LoginEvent.OnPasswordChange(it))
                },
                placeholder = {
                    Text(text = "Password")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password"
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                isError = viewModel.loginState.passwordErrorMessage != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )
            viewModel.loginState.passwordErrorMessage?.let {
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
                    viewModel.onEvent(LoginEvent.OnLoginClick)
                }) {
                Text(text = "Login")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.onEvent(LoginEvent.OnGotoRegisterClick)
                }) {
                Text(text = "Goto Register")
            }
        }
    }
}