package id.yuana.todo.compose.data.repository

import id.yuana.todo.compose.data.local.dao.TodoDao
import id.yuana.todo.compose.data.mapper.mapFromEntity
import id.yuana.todo.compose.data.mapper.mapToEntity
import id.yuana.todo.compose.data.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TodoRepository {

    suspend fun insertTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)

    suspend fun getTodoById(id: Int): Todo?

    fun getTodos(): Flow<List<Todo>>

    class Impl(
        private val todoDao: TodoDao
    ) : TodoRepository {

        override suspend fun insertTodo(todo: Todo) {
            todoDao.insertTodo(todo.mapToEntity())
        }

        override suspend fun deleteTodo(todo: Todo) {
            todoDao.deleteTodo(todo.mapToEntity())
        }

        override suspend fun getTodoById(id: Int): Todo? {
            return todoDao.getTodoById(id)?.mapFromEntity()
        }

        override fun getTodos(): Flow<List<Todo>> {
            return todoDao.getTodos().map { it.map { it.mapFromEntity() } }
        }

    }
}