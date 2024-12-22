package com.example.myapplication2.model

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TaskDao(private val db: SQLiteDatabase) {


    fun getAllTasks(): Flow<List<Task>> = flow {
        val cursor: Cursor = db.rawQuery("SELECT * FROM tasks", null)
        val tasks = mutableListOf<Task>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("due_date"))
            val priority = cursor.getString(cursor.getColumnIndexOrThrow("priority"))
            val isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted")) == 1

            tasks.add(Task(id, title, description, date, priority, isCompleted))
        }
        cursor.close()
        emit(tasks)
    }


    fun getTaskById(id: Long): Flow<Task?> = flow {
        val cursor: Cursor = db.rawQuery("SELECT * FROM tasks WHERE id = ?", arrayOf(id.toString()))
        val task: Task? = if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("due_date"))
            val priority = cursor.getString(cursor.getColumnIndexOrThrow("priority"))
            val isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted")) == 1

            Task(id, title, description, date, priority, isCompleted)
        } else {
            null
        }
        cursor.close()
        emit(task)
    }

    suspend fun insertTask(task: Task): Long {
        val values = ContentValues().apply {
            put("title", task.title)
            put("description", task.description)
            put("due_date", task.dueDate)
            put("priority", task.priority)
            put("isCompleted", if (task.isCompleted) 1 else 0)
        }
        return db.insert("tasks", null, values)
    }



    suspend fun updateTask(task: Task) {
        val values = ContentValues().apply {
            put("title", task.title)
            put("description", task.description)
            put("due_date", task.dueDate)
            put("priority", task.priority)
            put("isCompleted", if (task.isCompleted) 1 else 0)
        }
        db.update("tasks", values, "id = ?", arrayOf(task.id.toString()))
    }


    suspend fun deleteTask(task: Task) {
        db.delete("tasks", "id = ?", arrayOf(task.id.toString()))
    }


    suspend fun deleteAllTasks() {
        db.delete("tasks", null, null)
    }
}
