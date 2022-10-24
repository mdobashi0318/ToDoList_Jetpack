package com.dobashi.todolist_jetpack

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dobashi.todolist_jetpack.other.RegistrationDestination
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

                        DropdownMenuItem(onClick = { /*TODO*/ }) {
                            Text(text = stringResource(id = R.string.delete))
                        }
                    }
                }
            )
        },
    ) {
        Column(
            modifier
                .padding(top = 8.dp)
                .fillMaxSize()
        ) {
            DetailCard(
                title = stringResource(id = R.string.date_title),
                value = "${model.todoDate}\n${model.todoTime}"
            )

            DetailCard(
                title = stringResource(id = R.string.detail_title),
                value = model.toDoDetail
            )
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


