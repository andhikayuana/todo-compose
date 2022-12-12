package id.yuana.todo.compose.data.model

data class Todo(
    val title: String,
    val description: String?,
    val isDone: Boolean,
    val id: Int? = null
)
