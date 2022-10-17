package com.dobashi.todolist_jetpack

import android.app.Application
import androidx.room.Room
import com.dobashi.todolist_jetpack.model.ToDoModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TodoApplication : Application() {
    companion object {
        lateinit var database: TodoRoomDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            TodoRoomDatabase::class.java,
            "todo_database"
        ).build()


        val format = SimpleDateFormat("MMddHHmmS")
/*
        CoroutineScope(Dispatchers.IO).launch {
            database.todoDao()
                .add(
                    ToDoModel(
                        format.format(Date()).toString(),
                        "name${format.format(Date())}",
                        "date",
                        "time",
                        "detail",
                        "flag"
                    )
                )
        }
        */
    }

}