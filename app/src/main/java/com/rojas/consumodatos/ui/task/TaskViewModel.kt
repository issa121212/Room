package com.rojas.consumodatos.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rojas.consumodatos.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getTasks().collect { list ->
                _uiState.update { it.copy(tasks = list, isLoading = false) }
            }
        }
    }

    fun onTaskNameChanged(name: String) {
        _uiState.update { it.copy(taskNameInput = name) }
    }

    fun addTask() {
        val name = _uiState.value.taskNameInput.trim()
        if (name.isBlank()) return

        viewModelScope.launch {
            repository.addTask(name)
            _uiState.update { it.copy(taskNameInput = "") }
        }
    }
}
