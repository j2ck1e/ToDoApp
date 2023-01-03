package com.jcdesign.todoapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jcdesign.todoapp.ToDoItem

@Database(entities = [ToDoItem::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun todoDao(): ToDoDao
}