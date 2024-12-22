package com.example.myapplication2.repository

import android.content.Context
import android.util.Log
import com.example.myapplication2.model.Task
import com.example.myapplication2.model.TaskDao
import com.example.myapplication2.model.TaskDatabaseHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TaskRepository(context: Context) {

    private val dbHelper = TaskDatabaseHelper(context)
    private val taskDao = TaskDao(dbHelper.writableDatabase)


    fun getAllTasks(): Flow<List<Task>> = flow {
        taskDao.getAllTasks().collect { tasks ->
            Log.d("TaskRepository", "Fetched tasks: $tasks")
            emit(tasks)
        }
    }


    fun getTaskById(id: Long): Flow<Task?> {
        return taskDao.getTaskById(id)
    }


    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }


    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }


    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }


    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }
}
