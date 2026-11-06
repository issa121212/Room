package com.rojas.consumodatos.domain.repository

import com.rojas.consumodatos.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    suspend fun addTask(name: String)
}
