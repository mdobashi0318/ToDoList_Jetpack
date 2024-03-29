package com.dobashi.todolist_jetpack

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dobashi.todolist_jetpack.model.ToDoModel
import com.dobashi.todolist_jetpack.other.CompletionFlag
import com.dobashi.todolist_jetpack.other.Mode
import kotlinx.coroutines.runBlocking
import java.util.*


@Composable
fun TodoRegistrationScreen(
    createTime: String? = null,
    navController: NavController,
    todoRegistrationViewModel: TodoRegistrationViewModel = viewModel()
) {
    todoRegistrationViewModel.find(createTime)
    val context = LocalContext.current

    var name by rememberSaveable {
        mutableStateOf(todoRegistrationViewModel.name)
    }

    var year by rememberSaveable {
        mutableStateOf(todoRegistrationViewModel.year)
    }

    var month by rememberSaveable {
        mutableStateOf(todoRegistrationViewModel.month)
    }

    var day by rememberSaveable {
        mutableStateOf(todoRegistrationViewModel.day)
    }

    var isShowDatePicker by remember {
        mutableStateOf(false)
    }

    var hour by rememberSaveable {
        mutableStateOf(todoRegistrationViewModel.hour)
    }

    var min by rememberSaveable {
        mutableStateOf(todoRegistrationViewModel.min)
    }

    var detail by rememberSaveable {
        mutableStateOf(todoRegistrationViewModel.detail)
    }

    var isShowTimePicker by remember {
        mutableStateOf(false)
    }


    var isValidation by remember {
        mutableStateOf(false)
    }



    Scaffold(topBar = {
        TopAppBar(
            title = { Text(if (todoRegistrationViewModel.mode == Mode.Add) "作成" else "編集") },
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
    }) { padding ->

        if (isValidation) {
            AlertDialog(
                onDismissRequest = { isValidation = false },
                title = { Text(text = "未入力の箇所があります") },
                confirmButton = {
                    TextButton(onClick = { isValidation = false }) {
                        Text(text = "閉じる")
                    }
                },
            )
        }

        Column(modifier = Modifier.padding(padding)) {
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
                Row {
                    Text(
                        text = stringResource(id = R.string.dateFormat, year, month, day),
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = { isShowDatePicker = !isShowDatePicker }) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "")
                    }
                }

                if (isShowDatePicker) {
                    val datePicker = DatePickerDialog(
                        context,
                        { _: DatePicker, _year: Int, _month: Int, _day: Int ->
                            year = _year
                            month = _month + 1
                            day = _day
                            isShowDatePicker = false
                        }, year, month - 1, day
                    )
                    datePicker.setOnCancelListener {
                        isShowDatePicker = false
                    }
                    datePicker.show()
                }

            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 17.dp, vertical = 8.dp)
            ) {
                Text(text = "時間")
                Row() {
                    Text(
                        text = stringResource(id = R.string.timeFormat, hour, min),
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = { isShowTimePicker = !isShowTimePicker }) {
                        Icon(imageVector = Icons.Default.Timer, contentDescription = "")
                    }
                }

                if (isShowTimePicker) {
                    val timePicker = TimePickerDialog(
                        context,
                        { _: TimePicker, _hour: Int, _min: Int ->
                            hour = _hour
                            min = _min
                            isShowTimePicker = false
                        }, hour, min, true
                    )
                    timePicker.setOnCancelListener {
                        isShowTimePicker = false
                    }

                    timePicker.show()
                }

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
                onClick = {
                    if (name.isEmpty() || detail.isEmpty()) {
                        isValidation = true
                        return@Button
                    }

                    if (todoRegistrationViewModel.mode == Mode.Add) {
                        runBlocking {
                            try {
                                todoRegistrationViewModel.add(
                                    context,
                                    ToDoModel(
                                        createTime = todoRegistrationViewModel.format.format(Date())
                                            .toString(),
                                        toDoName = name,
                                        todoDate = "${year}/${month}/${day}",
                                        todoTime = "${hour}:${min}",
                                        toDoDetail = detail,
                                        completionFlag = CompletionFlag.Unfinished.value
                                    )
                                )
                                navController.navigateUp()
                            } catch (e: Exception) {
                                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                            }
                        }

                        if (todoRegistrationViewModel.isError) return@Button
                    } else {
                        runBlocking {
                            todoRegistrationViewModel.update(
                                context,
                                ToDoModel(
                                    createTime = todoRegistrationViewModel.createTime,
                                    toDoName = name,
                                    todoDate = "${year}/${month}/${day}",
                                    todoTime = "${hour}:${min}",
                                    toDoDetail = detail,
                                    completionFlag = CompletionFlag.Unfinished.value
                                )
                            )
                        }
                        navController.navigateUp()
                    }

                    Toast.makeText(
                        context,
                        "Todoを${
                            if (todoRegistrationViewModel.mode == Mode.Add) context.getString(R.string.add) else context.getString(
                                R.string.update
                            )
                        }しました",
                        Toast.LENGTH_SHORT
                    ).show()

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 17.dp,
                        vertical = 20.dp
                    )
            ) {
                Text(
                    if (todoRegistrationViewModel.mode == Mode.Add) stringResource(id = R.string.add) else stringResource(
                        id = R.string.update
                    )
                )
            }
        }
    }
}