package com.example.lab9

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val isExpanded: Boolean = false
)
