package id.yuana.todo.compose.ui.todo_add_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.yuana.todo.compose.data.model.Todo
import id.yuana.todo.compose.data.repository.TodoRepository
import id.yuana.todo.compose.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoAddEditViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var todo by mutableStateOf<Todo?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    val todoId: Int by lazy {
        savedStateHandle.get<Int>("todoId") ?: -1
    }

    init {
        if (todoId != -1) {
            viewModelScope.launch {
                todoRepository.getTodoById(todoId)?.let {
                    title = it.title
                    description = it.description ?: ""
                    todo = it
                }
            }
        }
    }

    fun onEvent(event: TodoAddEditEvent) {
        when (event) {
            is TodoAddEditEvent.OnDescriptionChange -> {
                description = event.description
            }
            TodoAddEditEvent.OnSaveTodoClick -> {
                viewModelScope.launch {
                    if (title.isBlank()) {
                        sendUiEvent(UiEvent.ShowSnackbar(
                            message = "The tittle can't be empty"
                        ))
                        return@launch
                    }
                    todoRepository.insertTodo(
                        Todo(
                            title = title,
                            description = description,
                            isDone = todo?.isDone ?: false,
                            id = todo?.id
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
                }

            }
            is TodoAddEditEvent.OnTitleChange -> {
                title = event.title
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}