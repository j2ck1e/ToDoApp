package com.jcdesign.todoapp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


class CustomDialog(
    var activity: MainActivity,
    private val isNewItem: Boolean,
    private val item: ToDoItem?,
) : Dialog(activity),
    View.OnClickListener {
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText

    //private lateinit var numberInput: EditText
    private lateinit var labelText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_template)

        initViews()
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
        //numberInput.setText(item?.number.toString())
    }

    private fun createNewItem() {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val titleFromPref = sharedPref.getString("titleKey", "")
        val descriptionFromPref = sharedPref.getString("descriptionKey", "")
        titleInput.setText(titleFromPref)
        descriptionInput.setText(descriptionFromPref)
        Log.d("isNewItem", "createNewItem")
    }

    private fun initViews() {
        okButton = findViewById(R.id.dialog_ok)
        cancelButton = findViewById(R.id.dialog_cancel)
        titleInput = findViewById(R.id.dialog_input_title)
        descriptionInput = findViewById(R.id.dialog_input_description)
        //numberInput = findViewById(R.id.dialog_input_number)
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
        //val inputNumberResult = numberInput.text.toString().toInt()
        if(inputTitleResult.isNotBlank()) {
            activity.updateItem(ToDoItem(item!!.id, inputTitleResult,
                inputDescriptionResult/*,
            inputNumberResult*/))
        }

    }

    private fun okNewItemBeenClicked() {
        var inputTitleResult = titleInput.text.toString()
        var inputDescriptionResult = descriptionInput.text.toString()
        //val inputNumberResult = numberInput.text.toString().toInt()
        if(inputTitleResult.isNotBlank()) {
            activity.addItem(ToDoItem(0, inputTitleResult,
                inputDescriptionResult/*,
            inputNumberResult*/))
            titleInput.text.clear()
            descriptionInput.text.clear()
        }
    }

    override fun onStop() {
        super.onStop()

        if (isNewItem) {
            val inputTitleResult = titleInput.text.toString()
            val inputDescriptionResult = descriptionInput.text.toString()
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putString("titleKey", inputTitleResult)
                putString("descriptionKey", inputDescriptionResult)
                apply()
            }
        }
    }
}