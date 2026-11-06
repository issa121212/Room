package com.rojas.consumodatos.domain.model

data class Task(
    val id: Long = 0,
    val name: String,
    val isCompleted: Boolean = false
)
