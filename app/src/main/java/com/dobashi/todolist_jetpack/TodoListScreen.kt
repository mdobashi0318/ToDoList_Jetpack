package com.dobashi.todolist_jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dobashi.todolist_jetpack.model.ToDoModel
import com.dobashi.todolist_jetpack.other.DetailDestination
import com.dobashi.todolist_jetpack.other.ListDestination
import kotlinx.coroutines.runBlocking

@Composable
fun TodoListScreen(modifier: Modifier = Modifier) {

    val todoModel = runBlocking {
        TodoApplication.database.todoDao().getAll()
    }
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ListDestination.route) {
        composable(ListDestination.route) {
            LazyColumn {
                items(todoModel) { todo ->
                    TodoRow(
                        todo = todo,
                        clickable = { navController.navigate("${DetailDestination.route}/${todo.createTime}") })
                }
            }
        }

        composable(
            route = DetailDestination.routeWithArgs,
            arguments = DetailDestination.arguments
        ) { arg ->
            TodoDetailScreen(
                createTime = arg.arguments?.getString(DetailDestination.argumentName).toString(),
                navController = navController
            )
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