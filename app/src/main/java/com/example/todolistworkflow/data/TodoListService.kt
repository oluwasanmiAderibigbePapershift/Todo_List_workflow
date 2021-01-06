package com.example.todolistworkflow.data

import retrofit2.http.GET

interface TodoListService {

    @GET("todos")
    suspend fun getTodos() : List<TodosItem>
}