package com.rojas.consumodatos.data.repository

import com.rojas.consumodatos.data.local.TaskLocalDataSource
import com.rojas.consumodatos.data.mapper.toDomain
import com.rojas.consumodatos.data.mapper.toEntity
import com.rojas.consumodatos.domain.model.Task
import com.rojas.consumodatos.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val localDataSource: TaskLocalDataSource
) : TaskRepository {

    override fun getTasks(): Flow<List<Task>> {
        return localDataSource.getTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addTask(name: String) {
        val task = Task(name = name, isCompleted = false)
        localDataSource.saveTask(task.toEntity())
    }
}
