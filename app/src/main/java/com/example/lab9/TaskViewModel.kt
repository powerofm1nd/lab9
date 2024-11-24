package com.example.lab9

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {

    private val _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> get() = _tasks

    init {
        _tasks.addAll(
            listOf(
                Task(1, "Task 1", "Description of Task 1"),
                Task(2, "Task 2", "Description of Task 2"),
                Task(3, "Task 3", "Description of Task 3")
            )
        )
    }

    fun toggleTaskCompletion(taskId: Int, isCompleted: Boolean) {
        val taskIndex = _tasks.indexOfFirst { it.id == taskId }
        if (taskIndex >= 0) {
            _tasks[taskIndex] = _tasks[taskIndex].copy(isCompleted = isCompleted)
        }
    }

    fun deleteTask(taskId: Int) {
        _tasks.removeAll { it.id == taskId }
    }

    fun toggleTaskDetails(taskId: Int) {
        val taskIndex = _tasks.indexOfFirst { it.id == taskId }
        if (taskIndex >= 0) {
            _tasks[taskIndex] = _tasks[taskIndex].copy(isExpanded = !_tasks[taskIndex].isExpanded)
        }
    }
}