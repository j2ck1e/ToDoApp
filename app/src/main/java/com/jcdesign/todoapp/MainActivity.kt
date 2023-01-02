package com.jcdesign.todoapp

import CustomAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var linearLayout: LinearLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter
    private lateinit var data: ArrayList<ToDoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.main_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        linearLayout = findViewById(R.id.main_no_items_container)
        fab = findViewById(R.id.main_fab)

        fab.setOnClickListener {
            val customDialog = CustomDialog(this)
            customDialog.show()
        }

        data = ArrayList<ToDoItem>()

        adapter = CustomAdapter(data)
        recyclerView.adapter = adapter
    }

    fun addItem(item: ToDoItem) {
        adapter.addItem(item)
        if (data.isEmpty()) {
            linearLayout.visibility = VISIBLE
            recyclerView.visibility = INVISIBLE
        } else {
            linearLayout.visibility = INVISIBLE
            recyclerView.visibility = VISIBLE
        }
    }
}