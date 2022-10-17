package com.dobashi.todolist_jetpack

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dobashi.todolist_jetpack.model.ToDoModel


@Database(entities = [ToDoModel::class], version = 1, exportSchema = false)
abstract class TodoRoomDatabase : RoomDatabase() {
    abstract fun todoDao(): ToDoModelDao
}