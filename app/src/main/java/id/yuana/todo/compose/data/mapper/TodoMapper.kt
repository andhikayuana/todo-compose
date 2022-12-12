package id.yuana.todo.compose.data.mapper

import id.yuana.todo.compose.data.local.entity.TodoEntity
import id.yuana.todo.compose.data.model.Todo

fun Todo.mapToEntity(): TodoEntity = TodoEntity(
    title, description, isDone, id
)

fun TodoEntity.mapFromEntity(): Todo = Todo(
    title, description, isDone, id
)