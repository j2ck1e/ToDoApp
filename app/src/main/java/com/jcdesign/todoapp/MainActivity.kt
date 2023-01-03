package com.jcdesign.todoapp

import CustomAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jcdesign.todoapp.room.AppDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var stubContainer: LinearLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter
    private lateinit var db: AppDatabase

    private lateinit var todoLiveData: LiveData<List<ToDoItem>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.main_recycler_view)
        stubContainer = findViewById(R.id.main_no_items_container)
        fab = findViewById(R.id.main_fab)

        fab.setOnClickListener {
            val dialog = CustomDialog(this)
            dialog.show()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CustomAdapter(mutableListOf())

        recyclerView.adapter = adapter

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name")
            .allowMainThreadQueries()
            .build()

        todoLiveData = db.todoDao().getAllItems()

        todoLiveData.observe(this, Observer {
            adapter.updateList(it)
            Log.d("MyLog", "-> $it")
            if (it.isEmpty()) {
                stubContainer.visibility = VISIBLE
                recyclerView.visibility = INVISIBLE
            } else {
                stubContainer.visibility = INVISIBLE
                recyclerView.visibility = VISIBLE
            }
        })

    }

    fun addItem(item: ToDoItem) {
        stubContainer.visibility = INVISIBLE
        recyclerView.visibility = VISIBLE
        db.todoDao().insertItem(item)
    }
}