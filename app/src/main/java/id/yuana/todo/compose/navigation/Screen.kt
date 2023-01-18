package id.yuana.todo.compose.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val args: List<NamedNavArgument> = listOf(),
) {
    object Login : Screen("/login")
    object Register : Screen("/register")
    object TodoList : Screen("/todos")
    object TodoAddEdit : Screen(
        route = "/todos/add-edit?${argTodoId}={${argTodoId}}",
        args = listOf(
            navArgument(name = argTodoId) {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) {
        fun routeWithArg(todoId: Int): String = route.replace("{$argTodoId}", todoId.toString())
    }

    companion object {
        const val argTodoId = "todoId"
    }
}
