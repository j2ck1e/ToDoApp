package com.jcdesign.todoapp.room


import androidx.lifecycle.LiveData
import androidx.room.*
import com.jcdesign.todoapp.ToDoItem

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todoitem") // name of table
    fun getAllItems(): LiveData<List<ToDoItem>>

    @Insert
    fun insertItem(toDoItem: ToDoItem)

    @Delete
    fun deleteItem(toDoItem: ToDoItem)

    @Update
    fun updateItem(toDoItem: ToDoItem)
}