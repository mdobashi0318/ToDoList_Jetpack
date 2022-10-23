package com.dobashi.todolist_jetpack

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dobashi.todolist_jetpack.extensions.addFirstZero
import com.dobashi.todolist_jetpack.model.ToDoModel
import com.dobashi.todolist_jetpack.other.Mode
import kotlinx.coroutines.runBlocking
import java.util.*


@Composable
fun TodoRegistrationScreen(
    createTime: String? = null,
    navController: NavController,
    todoRegistrationViewModel: TodoRegistrationViewModel = viewModel()
) {

    val context = LocalContext.current
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

        val calendar = Calendar.getInstance()

        var year by remember {
            mutableStateOf(calendar.get(Calendar.YEAR))
        }

        var month by remember {
            mutableStateOf(calendar.get(Calendar.MONTH) + 1)
        }

        var day by remember {
            mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))
        }

        var isShowDatePicker by remember {
            mutableStateOf(false)
        }

        var isShowTimePicker by remember {
            mutableStateOf(false)
        }

        var hour by remember {
            mutableStateOf(calendar.get(Calendar.HOUR))
        }

        var min by remember {
            mutableStateOf(
                calendar.get(Calendar.MINUTE)
            )
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
                Row {
                    Text(
                        text = "${year}/${month.toString().addFirstZero()}/${
                            day.toString().addFirstZero()
                        }", modifier = Modifier.weight(1f)
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
                        }, year, month, day
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
                        text = "${hour.toString().addFirstZero()}:${
                            min.toString().addFirstZero()
                        }", modifier = Modifier.weight(1f)
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
                    // TODO: 未入力チェック処理

                    if (mode == Mode.Add) {
                        runBlocking {
                            todoRegistrationViewModel.add(
                                ToDoModel(
                                    createTime = todoRegistrationViewModel.format.format(Date()).toString(),
                                    toDoName = name,
                                    todoDate = "${year}/${month}/${day}",
                                    todoTime = "${hour}:${min}",
                                    toDoDetail = detail,
                                    completionFlag = "completionFlag"
                                )
                            )
                        }
                        navController.navigateUp()
                    } else {
                        // TODO: 更新処理
                    }
                },
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