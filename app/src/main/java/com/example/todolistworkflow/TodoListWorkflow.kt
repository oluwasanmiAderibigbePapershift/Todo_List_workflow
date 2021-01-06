package com.example.todolistworkflow

import android.util.Log
import com.example.todolistworkflow.data.TodoListWorker
import com.example.todolistworkflow.data.TodosItem
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.action
import com.squareup.workflow1.runningWorker

object TodoListWorkflow : StatefulWorkflow<Unit, TodoListWorkflow.TodoNetworkState, Nothing, TodoListScreen >() {
    sealed class TodoNetworkState{
        object Loading : TodoNetworkState()
        data class Success(val data : List<TodosItem>) : TodoNetworkState()
        data class Error(val error : String, val throwable: Throwable) : TodoNetworkState()
    }

    //Would use dagger here
    val todoListWorker = TodoListWorker()

    override fun initialState(props: Unit, snapshot: Snapshot?): TodoNetworkState {
        //TODO how to you create the object from this
       Log.d("TestHello", snapshot?.bytes?.utf8().toString())
        val result = Class.forName("MyClass")
      return TodoNetworkState.Loading
    }

    override fun render(
        props: Unit,
        state: TodoNetworkState,
        context: RenderContext
    ): TodoListScreen {
       return when(state){
            TodoNetworkState.Loading -> {
                context.runningWorker(todoListWorker) {
                    onTodoListGotten(it)
                }
                TodoListScreen(loading = true)
            }
           is TodoNetworkState.Success -> {
               TodoListScreen(
                   loading = false,
                   todos = state.data,
                   error =  ""
               )
           }
           is TodoNetworkState.Error -> TodoListScreen(
               loading = false,
               todos = emptyList(),
               error = "$state.error ${state.throwable} "
           )
        }
    }

    override fun snapshotState(state: TodoNetworkState): Snapshot {
        return Snapshot.write { bufferedSink -> bufferedSink.writeUtf8(state.toString()) }
    }

    private fun onTodoListGotten(workerState: TodoListWorker.WorkerState) = action {
        state = when(workerState){
            is TodoListWorker.WorkerState.Success -> {
                TodoNetworkState.Success(workerState.data)
            }
            is TodoListWorker.WorkerState.Error -> {
                TodoNetworkState.Error(workerState.error, workerState.throwable)
            }
        }
    }
}