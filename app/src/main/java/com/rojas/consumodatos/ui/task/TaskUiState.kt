package com.rojas.consumodatos.ui.task

import com.rojas.consumodatos.domain.model.Task

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val taskNameInput: String = "",
    val isLoading: Boolean = false
)
