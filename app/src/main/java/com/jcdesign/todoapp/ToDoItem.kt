package com.jcdesign.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDoItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    val number: Int
)
