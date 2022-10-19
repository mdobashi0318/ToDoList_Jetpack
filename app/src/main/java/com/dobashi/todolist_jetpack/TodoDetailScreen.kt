package com.dobashi.todolist_jetpack

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
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
                }

            )
        },
    ) {
        Column(
            modifier
                .fillMaxSize()
        ) {
            Text(text = model.todoDate)
            Text(text = model.todoTime)
            Text(text = model.toDoDetail)
        }
    }
}