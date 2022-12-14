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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import id.yuana.todo.compose.data.repository.AuthRepository
import id.yuana.todo.compose.ui.login.LoginScreen
import id.yuana.todo.compose.ui.register.RegisterScreen
import id.yuana.todo.compose.ui.theme.TodoComposeTheme
import id.yuana.todo.compose.ui.todo_add_edit.TodoAddEditScreen
import id.yuana.todo.compose.ui.todo_list.TodoListScreen
import id.yuana.todo.compose.util.Routes
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
                    viewModel.isUserAuthenticated -> Routes.TODO_LIST
                    else -> Routes.LOGIN
                }
            ) {
                composable(Routes.LOGIN) {
                    LoginScreen(onNavigate = { event ->
                        navController.navigate(event.route) {
                            if (event.route == Routes.TODO_LIST && event.removeBackStack) {
                                popUpTo(Routes.LOGIN) {
                                    inclusive = true
                                }
                            }
                        }
                    })
                }
                composable(Routes.REGISTER) {
                    RegisterScreen()
                }
                composable(Routes.TODO_LIST) {
                    TodoListScreen(
                        onNavigate = { event ->
                            navController.navigate(event.route) {
                                if (event.route == Routes.LOGIN && event.removeBackStack) {
                                    popUpTo(Routes.TODO_LIST) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    )
                }
                composable(
                    route = "${Routes.TODO_ADD_EDIT}?todoId={todoId}",
                    arguments = listOf(
                        navArgument(name = "todoId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
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