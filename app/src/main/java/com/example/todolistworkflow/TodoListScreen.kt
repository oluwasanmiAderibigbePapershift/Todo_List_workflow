package com.example.todolistworkflow


import com.example.todolistworkflow.data.TodosItem

data class TodoListScreen(
    val loading : Boolean = false,
    val todos : List<TodosItem> = emptyList(),
    val error : String = ""
)