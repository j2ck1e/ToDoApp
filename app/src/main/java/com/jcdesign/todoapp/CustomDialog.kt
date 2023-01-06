package com.jcdesign.todoapp

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


class CustomDialog(var activity: MainActivity, private val isNewItem: Boolean, private val item: ToDoItem?) : Dialog(activity),
    View.OnClickListener {
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var numberInput: EditText
    private lateinit var labelText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_template)

        initView()
        dialogSizeControl()
        if (isNewItem) {
            //
            createNewItem()
        } else {
            //
            updateExistingItem()
        }


    }

    private fun updateExistingItem() {
        Log.d("isNewItem", "updateExistingItem")
        labelText.text = "Update Item"
        titleInput.setText(item?.title)
        descriptionInput.setText(item?.description)
        numberInput.setText(item?.number.toString())
    }

    private fun createNewItem() {
        Log.d("isNewItem", "createNewItem")
    }

    private fun initView() {
        okButton = findViewById(R.id.dialog_ok)
        cancelButton = findViewById(R.id.dialog_cancel)
        titleInput = findViewById(R.id.dialog_input_title)
        descriptionInput = findViewById(R.id.dialog_input_description)
        numberInput = findViewById(R.id.dialog_input_number)
        labelText = findViewById<TextView>(R.id.dialog_label)
        okButton.setOnClickListener(this)
        cancelButton.setOnClickListener(this)
    }

    private fun dialogSizeControl() {
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(this.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        this.window?.attributes = lp
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_ok -> okButtonClicker()
            R.id.dialog_cancel -> dismiss()
        }
    }

    private fun okButtonClicker() {
        if (isNewItem) {
            okNewItemBeenClicked()

        } else {
            okUpdateItemBeenClicked()
        }

        dismiss()
    }

    private fun okUpdateItemBeenClicked() {
        val inputTitleResult = titleInput.text.toString()
        val inputDescriptionResult = descriptionInput.text.toString()
        val inputNumberResult = numberInput.text.toString().toInt()
        activity.updateItem(ToDoItem(item!!.id, inputTitleResult,
            inputDescriptionResult,
            inputNumberResult))
    }

    private fun okNewItemBeenClicked() {
        val inputTitleResult = titleInput.text.toString()
        val inputDescriptionResult = descriptionInput.text.toString()
        val inputNumberResult = numberInput.text.toString().toInt()
        activity.addItem(ToDoItem(0, inputTitleResult,
            inputDescriptionResult,
            inputNumberResult))
    }
}