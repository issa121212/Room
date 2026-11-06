package com.rojas.consumodatos.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TaskLocalDataSource {
    fun getTasks(): Flow<List<TaskEntity>>
    suspend fun saveTask(task: TaskEntity)
}

class TaskLocalDataSourceImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskLocalDataSource {
    override fun getTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()
    override suspend fun saveTask(task: TaskEntity) = taskDao.insertTask(task)
}
