package id.yuana.todo.compose.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import id.yuana.todo.compose.data.repository.AuthRepository
import javax.inject.Inject

@Composable
fun TodoMainNavigation(
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val startDestination = when {
        viewModel.isUserAuthenticated -> Screen.TodoList.route
        else -> Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        TodoNavGraph(onNavigate = {
            if (it.clearBackStack) {
                navController.navigateAndReplaceStartRoute(it.route)
            } else {
                navController.navigate(it.route)
            }
        }, onPopBackStack = {
            navController.popBackStack()
        })
    }

}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val isUserAuthenticated = authRepository.isAuthenticated()
}

fun NavHostController.navigateAndReplaceStartRoute(route: String) {
    popBackStack(graph.startDestinationId, true)
    graph.setStartDestination(route)
    navigate(route)
}
