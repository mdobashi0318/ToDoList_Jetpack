package com.dobashi.todolist_jetpack

import android.content.Context
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import com.dobashi.todolist_jetpack.model.ToDoModel
import com.dobashi.todolist_jetpack.other.Mode
import com.dobashi.todolist_jetpack.other.Notification
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat

class TodoRegistrationViewModel : ViewModel() {

    private var _mode: Mode = Mode.Add

    val mode: Mode
        get() = _mode

    val format = SimpleDateFormat("MMddHHmmS")

    private val calendar = Calendar.getInstance()

    private lateinit var model: ToDoModel

    private lateinit var _createTime: String

    val createTime: String
        get() = _createTime

    var name = ""
    var detail = ""
    var year = calendar.get(Calendar.YEAR)
    var month = calendar.get(Calendar.MONTH) + 1
    var day = calendar.get(Calendar.DAY_OF_MONTH)


    private var time: MutableMap<String, Int> =
        mutableMapOf("hour" to calendar.get(Calendar.HOUR_OF_DAY), "min" to calendar.get(Calendar.MINUTE))

    var hour: Int
        get() = time["hour"] ?: 0
        set(value) {
            time["hour"] = value
        }

    var min: Int
        get() = time["min"] ?: 0
        set(value) {
            time["min"] = value
        }

    fun find(createTime: String?) {
        if (createTime != null) {
            _mode = Mode.Edit
            runBlocking {
                model = TodoApplication.database.todoDao().getTodo(createTime)
                _createTime = model.createTime
                name = model.toDoName
                detail = model.toDoDetail

                val tmpDate = model.todoDate.split("/")
                year = tmpDate[0].toInt()
                month = tmpDate[1].toInt()
                day = tmpDate[2].toInt()

                val tmpTime = model.todoTime.split(":")
                hour = tmpTime[0].toInt()
                min = tmpTime[1].toInt()
            }
        }


    }


    suspend fun add(context: Context, model: ToDoModel) {
        TodoApplication.database.todoDao().add(model)
        Notification.setNotification(context, model)
    }

    suspend fun update(context: Context,model: ToDoModel) {
        TodoApplication.database.todoDao().update(model)
        Notification.setNotification(context, model)
    }
}