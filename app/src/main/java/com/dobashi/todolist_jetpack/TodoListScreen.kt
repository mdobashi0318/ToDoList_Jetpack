package com.dobashi.todolist_jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dobashi.todolist_jetpack.model.ToDoModel
import com.dobashi.todolist_jetpack.other.DetailDestination

@Composable
fun TodoListScreen(
    todoModel: List<ToDoModel>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = stringResource(id = R.string.app_name)
            )
        })
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
                Text(text = todo.completionFlag)
            }
            Text(text = "${todo.todoDate} ${todo.todoTime}")
        }
    }

}