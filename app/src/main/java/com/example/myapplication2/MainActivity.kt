package com.example.myapplication2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.myapplication2.model.Task
import com.example.myapplication2.repository.TaskRepository
import com.example.myapplication2.ui.SettingsScreen
import com.example.myapplication2.ui.theme.MyApplication2Theme
import com.example.myapplication2.viewmodel.TaskViewModel
import com.example.myapplication2.viewmodel.TaskViewModelFactory
import com.example.myapplication2.viewmodel.ThemeViewModel
import com.example.myapplication2.viewmodel.ThemeViewModelFactory
import androidx.compose.material.icons.filled.ArrowDropDown
import java.time.LocalDate
import androidx.compose.ui.Alignment
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = applicationContext
        val taskRepository = TaskRepository(context)
        val taskViewModel = ViewModelProvider(this, TaskViewModelFactory(taskRepository))
            .get(TaskViewModel::class.java)

        val themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(context))
            .get(ThemeViewModel::class.java)

        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState(initial = false)


            MyApplication2Theme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        MyApp(navController, taskViewModel, themeViewModel)
                    }
                    composable("addTask") {
                        AddTaskScreen(navController, taskViewModel, themeViewModel)
                    }
                    composable("settings") {
                        SettingsScreen(themeViewModel)
                    }
                    composable("taskDetails/{taskId}") { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getString("taskId")?.toLongOrNull()
                        taskId?.let {
                            TaskDetailsScreen(taskId, taskViewModel, navController, themeViewModel)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun MyApp(navController: NavHostController, taskViewModel: TaskViewModel, themeViewModel: ThemeViewModel) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf("default") }

    val tasks by taskViewModel.tasks.collectAsState(initial = emptyList())


    val sortedTasks = when (sortOption) {
        "priority" -> tasks.sortedBy { it.priority }
        "date" -> tasks.sortedBy { it.dueDate }
        else -> tasks
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Завдання",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )


                Box(
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Sort",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Сортувати за пріоритетом") },
                            onClick = {
                                sortOption = "priority"
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Сортувати за датою") },
                            onClick = {
                                sortOption = "date"
                                isMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (sortedTasks.isEmpty()) {
                Text(
                    "Немає завдань",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(0.7f) // Измените на подходящую высоту
                ) {
                    items(sortedTasks) { task ->
                        TaskItem(
                            task = task,
                            onTaskClick = { navController.navigate("taskDetails/${task.id}") },
                            onTaskCompleted = { updatedTask -> taskViewModel.updateTask(updatedTask) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("addTask") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Додати нове завдання")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigate("settings") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Налаштування")
            }
        }
    }
}


@Composable
fun TaskItem(task: Task, onTaskClick: () -> Unit, onTaskCompleted: (Task) -> Unit) {
    val priorityColor = when (task.priority) {
        "Низький" -> Color.Green
        "Середній" -> Color.Yellow
        "Високий" -> Color.Red
        else -> Color.Gray
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small)
            .padding(12.dp)
            .clickable { onTaskClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(priorityColor, shape = MaterialTheme.shapes.small)
            )

            Spacer(modifier = Modifier.width(8.dp))


            Text(
                text = "${task.title} (${task.dueDate})",
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }


        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { isChecked -> onTaskCompleted(task.copy(isCompleted = isChecked)) }
        )
    }
}


@Composable
fun TaskDetailsScreen(
    taskId: Long,
    taskViewModel: TaskViewModel,
    navController: NavHostController,
    themeViewModel: ThemeViewModel
) {
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val task by taskViewModel.getTaskById(taskId).collectAsState(initial = null)
    val context = LocalContext.current

    MyApplication2Theme(darkTheme = isDarkTheme) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            task?.let { currentTask ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Деталі завдання",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        DetailItem(label = "Назва:", value = currentTask.title)
                        DetailItem(label = "Опис:", value = currentTask.description)
                        DetailItem(label = "Дата:", value = currentTask.dueDate)
                        DetailItem(label = "Пріоритет:", value = currentTask.priority)
                        DetailItem(label = "Виконано:", value = if (currentTask.isCompleted) "Так" else "Ні")

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                taskViewModel.deleteTask(currentTask)
                                Toast.makeText(context, "Завдання видалено", Toast.LENGTH_SHORT).show()
                                navController.popBackStack("home", inclusive = false)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Видалити", color = MaterialTheme.colorScheme.onError)
                        }
                    }
                }
            } ?: run {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Завдання не знайдено",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Divider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavHostController, taskViewModel: TaskViewModel, themeViewModel: ThemeViewModel) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf("Низький") }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val textColor = MaterialTheme.colorScheme.onBackground

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(16.dp))


                Text("Назва завдання", color = textColor)
                BasicTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, textColor, MaterialTheme.shapes.small)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))


                Text("Опис завдання", color = textColor)
                BasicTextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, textColor, MaterialTheme.shapes.small)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))


                Text("Дата завдання (день/місяць/рік)", color = textColor)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    BasicTextField(
                        value = day,
                        onValueChange = { if (it.length <= 2) day = it },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, textColor, MaterialTheme.shapes.small)
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = month,
                        onValueChange = { if (it.length <= 2) month = it },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, textColor, MaterialTheme.shapes.small)
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = year,
                        onValueChange = { if (it.length <= 4) year = it },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, textColor, MaterialTheme.shapes.small)
                            .padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))


                Text("Пріоритет завдання", color = textColor)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = selectedPriority,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown Icon")
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("Низький", "Середній", "Високий").forEach { priority ->
                            DropdownMenuItem(
                                text = { Text(priority) },
                                onClick = {
                                    selectedPriority = priority
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                Button(
                    onClick = {
                        val formattedDate = "$day.$month.$year"
                        val isFutureDate = try {
                            val inputDate = LocalDate.of(year.toInt(), month.toInt(), day.toInt())
                            inputDate.isAfter(LocalDate.now()) || inputDate.isEqual(LocalDate.now())
                        } catch (e: Exception) {
                            false
                        }

                        if (taskTitle.isNotEmpty() && taskDescription.isNotEmpty() && isFutureDate) {
                            taskViewModel.insertTask(
                                Task(
                                    id = 0,
                                    title = taskTitle,
                                    description = taskDescription,
                                    dueDate = formattedDate,
                                    priority = selectedPriority,
                                    isCompleted = false
                                )
                            )
                            Toast.makeText(context, "Завдання збережено", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Заповніть усі поля або введіть коректну дату", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Зберегти завдання")
                }

            }
        }
    }
}
