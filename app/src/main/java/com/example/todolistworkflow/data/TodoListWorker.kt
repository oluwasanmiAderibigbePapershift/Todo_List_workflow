package com.example.todolistworkflow.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.workflow1.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TodoListWorker : Worker<TodoListWorker.WorkerState> {

    sealed class WorkerState {
        data class Success(val data: List<TodosItem>) : WorkerState()
        data class Error(val error: String, val throwable: Throwable) : WorkerState()
    }

    //would use dagger here
    companion object API {

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val todoApi: TodoListService = retrofit.create(TodoListService::class.java)

    }

    override fun run(): Flow<WorkerState> = flow<WorkerState> {
        val result = todoApi.getTodos()
        emit(WorkerState.Success(result))
    }.catch { e -> emit(WorkerState.Error("Could not get list", e))  }
}