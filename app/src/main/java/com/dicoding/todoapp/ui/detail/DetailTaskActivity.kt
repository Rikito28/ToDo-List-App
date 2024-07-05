package com.dicoding.todoapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.ui.list.TaskActivity
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID

class DetailTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action
        val intent = intent.getIntExtra(TASK_ID, 1)
        val detailViewModelFactory = ViewModelFactory.getInstance(this)
        val detailViewModel = ViewModelProvider(this, detailViewModelFactory)[DetailTaskViewModel::class.java]
        val detailInputTitle: EditText = findViewById(R.id.detail_ed_title)
        val detailInputDescription: EditText = findViewById(R.id.detail_ed_description)
        val detailInputDueDate: EditText = findViewById(R.id.detail_ed_due_date)
        val detailBtn: Button = findViewById(R.id.btn_delete_task)


        detailViewModel.setTaskId(intent)
        detailViewModel.task.observe(this) { detail ->
            detailInputTitle.setText(detail.title)
            detailInputDescription.setText(detail.description)
            detailInputDueDate.setText(DateConverter.convertMillisToString(detail.dueDateMillis))

            detailBtn.setOnClickListener {
                detailViewModel.task.removeObservers(this)
                detailViewModel.deleteTask()
                Toast.makeText(this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, TaskActivity::class.java))
            }
        }


    }
}