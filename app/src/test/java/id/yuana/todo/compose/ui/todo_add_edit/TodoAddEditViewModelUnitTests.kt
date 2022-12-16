package id.yuana.todo.compose.ui.todo_add_edit

import androidx.lifecycle.SavedStateHandle
import id.yuana.todo.compose.CoroutineTestRule
import id.yuana.todo.compose.data.model.Todo
import id.yuana.todo.compose.data.repository.TodoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class TodoAddEditViewModelUnitTests {

    // Subject under test
    private lateinit var viewModel: TodoAddEditViewModel

    private lateinit var repository: TodoRepository
    private lateinit var savedStateHandle: SavedStateHandle

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
        repository = mock {
            onBlocking { getTodoById(TODO_ID) } doReturn TODO
        }
        savedStateHandle = mock()
    }

    @Test
    fun `when ViewModel is instantiated then get todoId from SavedStateHandle`() {
        // Given
        whenever(savedStateHandle.get<Int>("todoId")).thenReturn(TODO_ID)

        // When
        viewModel = TodoAddEditViewModel(repository, savedStateHandle)

        //  Then
        assertEquals(TODO_ID, viewModel.todoId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when ViewModel is instantiated and todoId is valid then get todo from repository`() = runTest {
        // Given
        whenever(savedStateHandle.get<Int>("todoId")).thenReturn(TODO_ID)

        // When
        viewModel = TodoAddEditViewModel(repository, savedStateHandle)

        //  Then
        verify(repository).getTodoById(any())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when ViewModel is instantiated and todoId is invalid then repository is not called`() = runTest {
        // Given
        whenever(savedStateHandle.get<Int>("todoId")).thenReturn(-1)

        // When
        viewModel = TodoAddEditViewModel(repository, savedStateHandle)

        //  Then
        verify(repository, never()).getTodoById(any())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when ViewModel is instantiated and get todo from repository then properties of todo are loaded`() = runTest {
        // Given
        whenever(savedStateHandle.get<Int>("todoId")).thenReturn(TODO_ID)

        // When
        viewModel = TodoAddEditViewModel(repository, savedStateHandle)

        // Then
        assertEquals(TODO.title, viewModel.title)
        assertEquals(TODO.description, viewModel.description)
        assertEquals(TODO, viewModel.todo)
    }

    @Test
    fun `on description change event, trigger change to description`() {
        // Given
        viewModel = TodoAddEditViewModel(repository, savedStateHandle)

        // When
        viewModel.onEvent(TodoAddEditEvent.OnDescriptionChange("some_description"))

        // Then
        assertEquals("some_description", viewModel.description)
    }

    @Test
    fun `on title change event, trigger change to title`() {
        // Given
        viewModel = TodoAddEditViewModel(repository, savedStateHandle)

        // When
        viewModel.onEvent(TodoAddEditEvent.OnTitleChange("some_title"))

        // Then
        assertEquals("some_title", viewModel.title)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `on save todo event, trigger insert todo`() = runTest {
        // Given
        whenever(savedStateHandle.get<Int>("todoId")).thenReturn(TODO_ID)
        viewModel = TodoAddEditViewModel(repository, savedStateHandle)

        // When
        viewModel.onEvent(TodoAddEditEvent.OnSaveTodoClick)

        // Then
        verify(repository).insertTodo(any())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `on save todo event and title is blank, don't trigger insert todo`() = runTest {
        // Given
        whenever(savedStateHandle.get<Int>("todoId")).thenReturn(TODO_ID)
        repository = mock {
            onBlocking { getTodoById(TODO_ID) } doReturn Todo(
                id = TODO_ID,
                title = "",
                description = "",
                isDone = false
            )
        }
        viewModel = TodoAddEditViewModel(repository, savedStateHandle)

        // Then
        verify(repository, never()).insertTodo(any())
    }




}