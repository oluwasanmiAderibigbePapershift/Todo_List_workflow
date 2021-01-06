package com.example.todolistworkflow


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.workflow1.ui.ViewRegistry
import com.squareup.workflow1.ui.WorkflowLayout
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.renderWorkflowIn
import kotlinx.coroutines.flow.StateFlow

@OptIn(WorkflowUiExperimentalApi::class)
val viewRegistry = ViewRegistry(
    TodoListLayoutRunner
)

@OptIn(WorkflowUiExperimentalApi::class)
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model : ValidateFormViewModel by viewModels()

        setContentView(
            WorkflowLayout(this).apply { start(model.renderings,viewRegistry) }
        )
    }


}

@OptIn(WorkflowUiExperimentalApi::class)
class ValidateFormViewModel(savedState: SavedStateHandle) : ViewModel() {

    val renderings: StateFlow<Any> by lazy {
        renderWorkflowIn(
            workflow = TodoListWorkflow,
            prop = Unit,
            scope = viewModelScope,
            savedStateHandle = savedState
        )
    }

}