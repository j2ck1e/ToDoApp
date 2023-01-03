package com.jcdesign.todoapp

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText


class CustomDialog(var activity: MainActivity) : Dialog(activity), View.OnClickListener {
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var numberInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_template)

        initView()
        dialogSizeControl()

    }

    private fun initView() {
        okButton = findViewById(R.id.dialog_ok)
        cancelButton = findViewById(R.id.dialog_cancel)
        titleInput = findViewById(R.id.dialog_input_title)
        descriptionInput = findViewById(R.id.dialog_input_description)
        numberInput = findViewById(R.id.dialog_input_number)
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
        val inputTitleResult = titleInput.text.toString()
        val inputDescriptionResult = descriptionInput.text.toString()
        val inputNumberResult = numberInput.text.toString().toInt()
        activity.addItem(ToDoItem(0, inputTitleResult,
            inputDescriptionResult,
            inputNumberResult))
        dismiss()
    }
}