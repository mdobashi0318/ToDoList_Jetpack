package com.dobashi.todolist_jetpack

import android.app.Application
import androidx.room.Room
import com.dobashi.todolist_jetpack.model.TodoRoomDatabase
import com.dobashi.todolist_jetpack.other.Notification

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

        Notification.createChannel(context = applicationContext)
    }

}