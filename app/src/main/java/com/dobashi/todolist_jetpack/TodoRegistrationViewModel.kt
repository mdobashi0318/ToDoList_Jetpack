package com.dobashi.todolist_jetpack

import androidx.lifecycle.ViewModel
import com.dobashi.todolist_jetpack.model.ToDoModel
import java.text.SimpleDateFormat

class TodoRegistrationViewModel : ViewModel() {
    val format = SimpleDateFormat("MMddHHmmS")

    suspend fun add(model: ToDoModel) {
        TodoApplication.database.todoDao().add(model)
    }

}