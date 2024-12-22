package com.example.myapplication2.model


data class Task(
    val id: Long = 0,           // id задачи
    val title: String,          // Название задачи
    val description: String,    // Описание задачи
    val dueDate: String,        // Дата в формате
    val priority: String,       // Приоритет задачи
    var isCompleted: Boolean = false  // Состояние выполнения задачи
)
