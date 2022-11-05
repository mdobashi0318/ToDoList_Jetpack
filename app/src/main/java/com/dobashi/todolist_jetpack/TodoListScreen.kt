package com.dobashi.todolist_jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dobashi.todolist_jetpack.model.ToDoModel
import com.dobashi.todolist_jetpack.other.CompletionFlag
import com.dobashi.todolist_jetpack.other.DetailDestination
import com.dobashi.todolist_jetpack.other.RegistrationDestination
import kotlinx.coroutines.runBlocking

@Composable
fun TodoListScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val todoModel = runBlocking {
        TodoApplication.database.todoDao().getAll()
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = stringResource(id = R.string.app_name)
            )
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = { navController.navigate(RegistrationDestination.route) }) {
            Icon(Icons.Default.Add, "Add")
        }

    }) {
        LazyColumn {
            items(todoModel) { todo ->
                TodoRow(
                    todo = todo,
                    clickable = { navController.navigate("${DetailDestination.route}/${todo.createTime}") })
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
                Text(modifier = Modifier.padding(start = 10.dp),
                    text = if (CompletionFlag.getCompletionFlag(todo.completionFlag)) stringResource(
                        id = R.string.completion
                    ) else stringResource(id = R.string.unfinished)
                )
            }
            Text(text = "${todo.todoDate} ${todo.todoTime}")
        }
    }

}