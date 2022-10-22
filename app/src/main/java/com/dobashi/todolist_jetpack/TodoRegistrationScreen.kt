package com.dobashi.todolist_jetpack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dobashi.todolist_jetpack.other.Mode

@Composable
fun TodoRegistrationScreen(
    createTime: String? = null,
    navController: NavController
) {
    val mode: Mode = if (createTime == null) Mode.Add else Mode.Edit

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(if (mode == Mode.Add) "作成" else "編集") },
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
        )
    }) {

        var name by remember {
            mutableStateOf("")
        }

        var date by remember {
            mutableStateOf("")
        }

        var time by remember {
            mutableStateOf("")
        }

        var detail by remember {
            mutableStateOf("")
        }
        Column(modifier = Modifier.padding(top = 8.dp)) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 17.dp, vertical = 8.dp)
            ) {
                Text(text = "名前")
                TextField(
                    value = name, onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 17.dp, vertical = 8.dp)
            ) {
                Text(text = "日付")
                // TODO: DatePicker
                TextField(
                    value = date, onValueChange = { date = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 17.dp, vertical = 8.dp)
            ) {
                Text(text = "時間")
                // TODO: TimePicker
                TextField(
                    value = time, onValueChange = { time = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 17.dp, vertical = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.detail_title))
                TextField(
                    value = detail, onValueChange = { detail = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 17.dp,
                        vertical = 20.dp
                    )
            ) {
                Text(if (mode == Mode.Add) stringResource(id = R.string.add) else stringResource(id = R.string.edit))
            }
        }
    }

}