package com.example.lab9
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.lab9.ui.theme.Lab9Theme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab9Theme {
                TaskList(taskViewModel = taskViewModel)
            }
        }
    }

    @Composable
    fun TaskItem(
        task: Task,
        onCheckedChange: (Boolean) -> Unit,
        onDelete: () -> Unit,
        onToggleDetails: () -> Unit
    ) {
        var isExpanded by rememberSaveable { mutableStateOf(task.isExpanded) }
        val isVisible = rememberSaveable { mutableStateOf(true) }

        AnimatedVisibility(
            visible = isVisible.value,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .animateContentSize()
                    .shadow(4.dp),
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Checkbox(
                            checked = task.isCompleted,
                            onCheckedChange = onCheckedChange
                        )
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Task")
                        }
                    }
                    AnimatedVisibility(visible = isExpanded) {
                        Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
                    }
                    TextButton(onClick = { isExpanded = !isExpanded }) {
                        Text(if (isExpanded) "Hide Details" else "Show Details")
                    }
                }
            }
        }
    }

    @Composable
    fun TaskList(taskViewModel: TaskViewModel) {
        val tasks = taskViewModel.tasks
        val completedTaskCount = tasks.count { it.isCompleted }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Виконаних завдань: $completedTaskCount",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )

            LazyColumn {
                items(tasks, key = { it.id }) { task ->
                    var isVisible by rememberSaveable { mutableStateOf(true) }

                    LaunchedEffect(isVisible) {
                        if (!isVisible) {
                            delay(1000)
                            taskViewModel.deleteTask(task.id)
                        }
                    }

                    AnimatedVisibility(
                        visible = isVisible,
                        exit = fadeOut(animationSpec = tween(1000))
                    ) {
                        TaskItem(
                            task = task,
                            onCheckedChange = { isChecked ->
                                taskViewModel.toggleTaskCompletion(task.id, isChecked)
                            },
                            onDelete = {
                                isVisible = false
                            },
                            onToggleDetails = {
                                taskViewModel.toggleTaskDetails(task.id)
                            }
                        )
                    }
                }
            }
        }
    }
}