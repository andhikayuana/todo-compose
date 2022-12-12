package id.yuana.todo.compose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.yuana.todo.compose.data.local.dao.TodoDao
import id.yuana.todo.compose.data.local.entity.TodoEntity

@Database(
    entities = [TodoEntity::class],
    version = 1
)
abstract class TodoDatabase : RoomDatabase() {

    abstract val todoDao: TodoDao
}