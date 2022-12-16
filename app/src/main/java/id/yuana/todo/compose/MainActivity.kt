package id.yuana.todo.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import id.yuana.todo.compose.data.repository.AuthRepository
import id.yuana.todo.compose.navigation.Screen
import id.yuana.todo.compose.ui.login.LoginScreen
import id.yuana.todo.compose.ui.register.RegisterScreen
import id.yuana.todo.compose.ui.theme.TodoComposeTheme
import id.yuana.todo.compose.ui.todo_add_edit.TodoAddEditScreen
import id.yuana.todo.compose.ui.todo_list.TodoListScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoAppMainScreen()
        }
    }
}

@Composable
fun TodoAppMainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    TodoComposeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = when {
                    viewModel.isUserAuthenticated -> Screen.TodoList.route
                    else -> Screen.Login.route
                }
            ) {
                composable(Screen.Login.route) {
                    LoginScreen(onNavigate = { event ->
                        navController.navigate(event.route) {
                            if (event.clearBackStack) {
                                popUpTo(0)
                            }
                        }
                    })
                }
                composable(Screen.Register.route) {
                    RegisterScreen(
                        onNavigate = { event ->
                            navController.navigate(event.route) {
                                if (event.clearBackStack) {
                                    popUpTo(0)
                                }
                            }
                        },
                        onPopBackStack = { navController.popBackStack() }
                    )
                }
                composable(Screen.TodoList.route) {
                    TodoListScreen(
                        onNavigate = { event ->
                            navController.navigate(event.route) {
                                if (event.clearBackStack) {
                                    popUpTo(0)
                                }
                            }
                        }
                    )
                }
                composable(
                    route = Screen.TodoAddEdit.route,
                    arguments = Screen.TodoAddEdit.args
                ) {
                    TodoAddEditScreen(onPopBackStack = {
                        navController.popBackStack()
                    })
                }
            }
        }
    }
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val isUserAuthenticated = authRepository.isAuthenticated()
}