package id.yuana.todo.compose.ui.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.yuana.todo.compose.data.model.Todo
import id.yuana.todo.compose.data.repository.AuthRepository
import id.yuana.todo.compose.data.repository.TodoRepository
import id.yuana.todo.compose.navigation.Screen
import id.yuana.todo.compose.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val todos = todoRepository.getTodos()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTodo: Todo? = null

    fun onEvent(event: TodoListEvent) {
        when (event) {
            is TodoListEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Screen.TodoAddEdit.routeWithArg(event.todo.id ?: -1)))
            }
            is TodoListEvent.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    deletedTodo = event.todo
                    todoRepository.deleteTodo(event.todo)
                    sendUiEvent(
                        UiEvent.ShowSnackbar(
                            message = "Todo deleted",
                            action = "Undo"
                        )
                    )
                }
            }
            TodoListEvent.OnAddTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Screen.TodoAddEdit.route))
            }
            is TodoListEvent.OnDoneChange -> {
                viewModelScope.launch {
                    todoRepository.insertTodo(
                        event.todo.copy(isDone = event.isDone)
                    )
                }
            }
            TodoListEvent.OnUndoDeleteClick -> {
                deletedTodo?.let { todo ->
                    viewModelScope.launch {
                        todoRepository.insertTodo(todo)
                    }
                }
            }
            TodoListEvent.OnLogoutClick -> {
                viewModelScope.launch {
                    authRepository.signOut()
                    todoRepository.clearTodos()
                    sendUiEvent(
                        UiEvent.Navigate(
                            route = Screen.Login.route,
                            clearBackStack = true
                        )
                    )
                }
            }
            TodoListEvent.ShowLogoutDialog -> {
                sendUiEvent(UiEvent.ShowDialog)
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}