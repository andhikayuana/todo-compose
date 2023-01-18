package id.yuana.todo.compose.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import id.yuana.todo.compose.ui.login.LoginScreen
import id.yuana.todo.compose.ui.register.RegisterScreen
import id.yuana.todo.compose.ui.todo_add_edit.TodoAddEditScreen
import id.yuana.todo.compose.ui.todo_list.TodoListScreen
import id.yuana.todo.compose.util.UiEvent

typealias OnNavigate = (UiEvent.Navigate) -> Unit
typealias OnPopBackStack = () -> Unit

fun NavGraphBuilder.TodoNavGraph(
    onNavigate: OnNavigate,
    onPopBackStack: OnPopBackStack
) {
    composable(Screen.Login.route) {
        LoginScreen(onNavigate = onNavigate)
    }
    composable(Screen.Register.route) {
        RegisterScreen(
            onNavigate = onNavigate,
            onPopBackStack = onPopBackStack
        )
    }
    composable(Screen.TodoList.route) {
        TodoListScreen(
            onNavigate = onNavigate
        )
    }
    composable(
        route = Screen.TodoAddEdit.route,
        arguments = Screen.TodoAddEdit.args
    ) {
        TodoAddEditScreen(onPopBackStack = onPopBackStack)
    }
}