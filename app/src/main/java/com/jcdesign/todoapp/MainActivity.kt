package com.jcdesign.todoapp

import CustomAdapter
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.jcdesign.todoapp.room.AppDatabase

class MainActivity : AppCompatActivity(), OnItemClick {
    private lateinit var stubContainer: LinearLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter
    private lateinit var db: AppDatabase

    private lateinit var todoLiveData: LiveData<List<ToDoItem>>
    private lateinit var data: List<ToDoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.main_recycler_view)
        stubContainer = findViewById(R.id.main_no_items_container)
        fab = findViewById(R.id.main_fab)

        fab.setOnClickListener {
            val dialog = CustomDialog(this, true, null)
            dialog.show()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CustomAdapter(mutableListOf(), this)

        recyclerView.adapter = adapter

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name")
            .allowMainThreadQueries()
            .build()

        todoLiveData = db.todoDao().getAllItems()

        todoLiveData.observe(this, Observer {
            data = it
            adapter.updateList(it)
            Log.d("MyLog", "-> $it")
            screenDataValidation(it)
        })

        // Delete function
        val deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_delete_24)
        val intrinsicWidth = deleteIcon?.intrinsicWidth
        val intrinsicHeight = deleteIcon?.intrinsicHeight
        val background = ColorDrawable()
        val backgroundColor = Color.parseColor("#f44336")
        val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
        //Left swipe

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                target: ViewHolder,
            ): Boolean {

                // for future implementation

                return false
            }

            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean,
            ) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top

                background.color = backgroundColor
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                background.draw(canvas)

                val iconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
                val iconMargin = (itemHeight - intrinsicHeight) / 2
                val iconLeft = itemView.right - iconMargin - intrinsicWidth!!
                val iconRight = itemView.right - iconMargin
                val iconBottom = iconTop + intrinsicHeight

                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                deleteIcon.draw(canvas)
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {

                val deletedToDoItem: ToDoItem = data.get(viewHolder.adapterPosition)
                val position = viewHolder.adapterPosition
                data.toMutableList().removeAt(position)
                adapter.notifyItemRemoved(position)
                Snackbar.make(recyclerView,
                    "Deleted " + deletedToDoItem.title,
                    Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo",
                        View.OnClickListener {
                            data.toMutableList().add(position, deletedToDoItem)
                            adapter.notifyItemInserted(position)
                        }).show()
                deleteItem(deletedToDoItem)
            }
        }).attachToRecyclerView(recyclerView)


    }

    private fun screenDataValidation(list: List<ToDoItem>) {
        if (list.isEmpty()) {
            stubContainer.visibility = VISIBLE
            recyclerView.visibility = INVISIBLE
        } else {
            stubContainer.visibility = INVISIBLE
            recyclerView.visibility = VISIBLE
        }
    }

    fun addItem(item: ToDoItem) {
        stubContainer.visibility = INVISIBLE
        recyclerView.visibility = VISIBLE
        db.todoDao().insertItem(item)
    }

    fun updateItem(item: ToDoItem) {
        db.todoDao().updateItem(item)
    }

    fun deleteItem(item: ToDoItem) {
        db.todoDao().deleteItem(item)
    }

    override fun itemClicked(item: ToDoItem) {
        Log.d("itemClicked", "itemClicked - > $item")
        val dialog = CustomDialog(this, false, item)
        dialog.show()
    }
}