package com.example.todolistworkflow

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

    override fun snapshotState(state: TodoNetworkState): Snapshot? {
        return null
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