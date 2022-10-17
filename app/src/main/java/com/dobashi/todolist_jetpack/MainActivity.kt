package com.dobashi.todolist_jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dobashi.todolist_jetpack.model.ToDoModel
import com.dobashi.todolist_jetpack.ui.theme.ToDoList_JetpackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoList_JetpackTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TodoListScreen()
                }
            }
        }
    }
}


@Composable
fun TodoListScreen(modifier: Modifier = Modifier) {

    val todoModel = arrayOf<ToDoModel>(
        ToDoModel(
            "createTine",
            "name1",
            "date",
            "time",
            "detail",
            "flag"
        ),
        ToDoModel(
            "createTine",
            "name2",
            "date",
            "time",
            "detail",
            "flag"
        ),
        ToDoModel(
            "createTine",
            "name3",
            "date",
            "time",
            "detail",
            "flag"
        )
    )
    LazyColumn {
        items(todoModel) { todo ->
            TodoItem(todo = todo)
        }
    }
}


@Composable
fun TodoItem(todo: ToDoModel, modifier: Modifier = Modifier) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth()
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
