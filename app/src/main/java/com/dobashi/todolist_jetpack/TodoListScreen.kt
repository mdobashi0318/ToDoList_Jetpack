package com.dobashi.todolist_jetpack

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dobashi.todolist_jetpack.extensions.splitDate
import com.dobashi.todolist_jetpack.extensions.splitTime
import com.dobashi.todolist_jetpack.model.ToDoModel
import com.dobashi.todolist_jetpack.other.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TodoListScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val pagerState = rememberPagerState(0)

    var todoModel = runBlocking {
        TodoApplication.database.todoDao()
            .getTodos(if (pagerState.currentPage == CompletionFlag.Completion.value.toInt()) CompletionFlag.Completion.value else CompletionFlag.Unfinished.value)
    }


    var isAllDelete by remember {
        mutableStateOf(false)
    }


    var titles = listOf<String>(
        stringResource(id = R.string.unfinished),
        stringResource(id = R.string.completion)
    )

    val context = LocalContext.current

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = stringResource(id = R.string.app_name)
            )
        }, actions = {
            IconButton(onClick = {
                isAllDelete = true
            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = { navController.navigate(RegistrationDestination.route) }) {
            Icon(Icons.Default.Add, "Add")
        }

    }) { padding ->
        Column(
            Modifier
                .padding(padding)
        ) {

            TabRow(selectedTabIndex = pagerState.currentPage) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            runBlocking {
                                pagerState.scrollToPage(index)
                            }
                        },
                        modifier = Modifier
                            .height(50.dp)
                    ) {
                        Text(
                            text = title
                        )
                    }
                }
            }

            HorizontalPager(
                titles.count(),
                state = pagerState
            ) {
                if (isAllDelete) {
                    AlertDialog(onDismissRequest = { isAllDelete = false },
                        title = { Text(text = "全件削除しますか？") },
                        confirmButton = {
                            Button(onClick = {
                                allDelete(context)
                                todoModel = listOf()
                                isAllDelete = false
                                Toast.makeText(context, "全件削除しました", Toast.LENGTH_SHORT).show()
                            }) {
                                Text(text = "削除")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { isAllDelete = false }) {
                                Text(text = "キャンセル")
                            }
                        })
                }

                if (todoModel.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.noTodo),
                        modifier = Modifier
                            .padding(top = 18.dp, start = 18.dp)
                            .fillMaxHeight()
                    )
                    return@HorizontalPager
                }

                LazyColumn(modifier.fillMaxHeight()) {
                    items(todoModel) { todo ->
                        TodoRow(
                            todo = todo,
                            clickable = { navController.navigate("${DetailDestination.route}/${todo.createTime}") })
                    }
                }
            }
        }
    }
}


@Composable
private fun TodoRow(todo: ToDoModel, clickable: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .clickable {
                clickable()
            }
    ) {
        Column(modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
            Row() {
                Text(text = todo.toDoName)
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = if (CompletionFlag.getCompletionFlag(todo.completionFlag)) stringResource(
                        id = R.string.completion
                    ) else stringResource(id = R.string.unfinished)
                )
            }
            val date = todo.todoDate.splitDate()
            val time = todo.todoTime.splitTime()
            Text(
                text = "${
                    stringResource(
                        id = R.string.dateFormat,
                        date[0],
                        date[1],
                        date[2]
                    )
                } ${
                    stringResource(
                        id = R.string.timeFormat,
                        time[0],
                        time[1]
                    )
                }"
            )
        }
    }

}


private fun allDelete(context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        TodoApplication.database.todoDao().deleteAll()
        Notification.cancelAllNotification(context)
    }
}