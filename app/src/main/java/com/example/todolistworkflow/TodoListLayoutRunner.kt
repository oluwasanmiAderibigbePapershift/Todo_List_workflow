package com.example.todolistworkflow

import android.view.View
import com.example.todolistworkflow.databinding.ActivityMainBinding
import com.squareup.workflow1.ui.LayoutRunner
import com.squareup.workflow1.ui.LayoutRunner.Companion.bind
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.ViewFactory
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

@OptIn(WorkflowUiExperimentalApi::class)
class TodoListLayoutRunner(private val binding : ActivityMainBinding) : LayoutRunner<TodoListScreen> {
    override fun showRendering(rendering: TodoListScreen, viewEnvironment: ViewEnvironment) {
        binding.progressBar.visibility = if(rendering.loading) View.VISIBLE else View.INVISIBLE
        binding.textView.text = rendering.todos.toString()
        binding.txtError.text = if(rendering.error.isNotEmpty()) rendering.error else ""

    }

    companion object : ViewFactory<TodoListScreen> by bind(
        ActivityMainBinding::inflate, ::TodoListLayoutRunner
    )
}