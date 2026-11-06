package com.rojas.consumodatos.data.mapper

import com.rojas.consumodatos.data.local.TaskEntity
import com.rojas.consumodatos.domain.model.Task

fun TaskEntity.toDomain() = Task(id = id, name = name, isCompleted = isCompleted)
fun Task.toEntity() = TaskEntity(id = id, name = name, isCompleted = isCompleted)
