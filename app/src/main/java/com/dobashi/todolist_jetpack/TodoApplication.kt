package com.dobashi.todolist_jetpack

import android.app.Application
import androidx.room.Room
import com.dobashi.todolist_jetpack.model.TodoRoomDatabase
import java.text.SimpleDateFormat

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