package com.dobashi.todolist_jetpack

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dobashi.todolist_jetpack.extensions.splitDate
import com.dobashi.todolist_jetpack.extensions.splitTime
import com.dobashi.todolist_jetpack.model.ToDoModel
import com.dobashi.todolist_jetpack.other.CompletionFlag
import com.dobashi.todolist_jetpack.other.Notification
import com.dobashi.todolist_jetpack.other.RegistrationDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@Composable
fun TodoDetailScreen(
    createTime: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val model = runBlocking {
        TodoApplication.database.todoDao().getTodo(createTime)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var isCompletion by remember {
        mutableStateOf(CompletionFlag.getCompletionFlag(model.completionFlag))
    }
    if (model == null) return

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = model.toDoName) },
                modifier = Modifier.background(color = MaterialTheme.colors.primary),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.MoreVert, "")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(onClick = {
                            navController.navigate("${RegistrationDestination.route}/${createTime}")
                        }) {
                            Text(text = stringResource(id = R.string.edit))
                        }

                        DropdownMenuItem(onClick = {
                            delete(context, model)
                            navController.navigateUp()
                            Toast.makeText(context, "削除しました", Toast.LENGTH_SHORT).show()
                        }) {
                            Text(text = stringResource(id = R.string.delete))
                        }
                    }
                }
            )
        },
    ) { padding ->
        Column(
            modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val date = model.todoDate.splitDate()
            val time = model.todoTime.splitTime()
            DetailCard(
                title = stringResource(id = R.string.date_title),
                value = "${
                    stringResource(
                        id = R.string.dateFormat,
                        date[0],
                        date[1],
                        date[2]
                    )
                }\n${
                    stringResource(
                        id = R.string.timeFormat,
                        time[0],
                        time[1]
                    )
                }"
            )

            DetailCard(
                title = stringResource(id = R.string.detail_title),
                value = model.toDoDetail
            )

            Card(
                elevation = 8.dp,
                modifier = Modifier
                    .padding(
                        horizontal = 17.dp,
                        vertical = 8.dp
                    )
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 17.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.complete_title),
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 10.dp)
                    )
                    Switch(checked = isCompletion,
                        onCheckedChange = {
                            model.completionFlag =
                                if (it) CompletionFlag.Completion.value else CompletionFlag.Unfinished.value
                            updateFlag(model)
                            isCompletion = !isCompletion
                            if (isCompletion) {
                                Notification.cancelNotification(context, createTime)
                            } else {
                                Notification.setNotification(context, model)
                            }
                        })
                }
            }
        }
    }
}


@Composable
private fun DetailCard(title: String, value: String) {
    Card(
        elevation = 8.dp,
        modifier = Modifier
            .padding(
                horizontal = 17.dp,
                vertical = 8.dp
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 8.dp
            )
        ) {
            Text(text = title)
            Text(text = value)
        }
    }

}


private fun delete(context: Context, model: ToDoModel) {
    CoroutineScope(Dispatchers.IO).launch {
        TodoApplication.database.todoDao().delete(model)
        Notification.cancelNotification(context, model.createTime)
    }
}


private fun updateFlag(model: ToDoModel) {
    CoroutineScope(Dispatchers.IO).launch {
        TodoApplication.database.todoDao().update(model)
    }
}


