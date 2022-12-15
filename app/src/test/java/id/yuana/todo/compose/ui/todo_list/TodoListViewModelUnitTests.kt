package id.yuana.todo.compose.ui.todo_list

import id.yuana.todo.compose.CoroutineTestRule
import id.yuana.todo.compose.data.model.Todo
import id.yuana.todo.compose.data.repository.AuthRepository
import id.yuana.todo.compose.data.repository.TodoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify


class TodoListViewModelUnitTests {

    // Subject under test
    private lateinit var viewModel: TodoListViewModel

    private lateinit var todoRepository: TodoRepository
    private lateinit var authRepository: AuthRepository

    // Constants
    private val TODO_ID = 1
    private val TODO = Todo(
        id = TODO_ID,
        title = "title",
        description = "description",
        isDone = false
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        todoRepository = mock()
        authRepository = mock()
        viewModel = TodoListViewModel(todoRepository, authRepository)
    }

    @Test
    fun `on delete event, trigger repository deleteTodo called`() = runTest {
        // When
        viewModel.onEvent(TodoListEvent.OnDeleteTodoClick(TODO))

        // Then
        verify(todoRepository).deleteTodo(TODO)
    }

    @Test
    fun `on done change event, trigger repository insertTodo called`() = runTest {
        // When
        val isDone = true
        viewModel.onEvent(TodoListEvent.OnDoneChange(TODO, isDone))

        // Then
        verify(todoRepository).insertTodo(TODO.copy(isDone = isDone))
    }
}