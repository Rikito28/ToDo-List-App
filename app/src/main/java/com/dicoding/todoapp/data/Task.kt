package com.dicoding.todoapp.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//TODO 1 : Define a local database table using the schema in app/schema/tasks.json
@Entity(tableName = "tasks")
data class Task(
    @ColumnInfo("id")
    @PrimaryKey(true)
    val id: Int = 0,

    @ColumnInfo("title")
    val title: String,

    @ColumnInfo("description")
    val description: String,

    @ColumnInfo("dueDateMillis")
    val dueDateMillis: Long,

    @ColumnInfo("Completed")
    val isCompleted: Boolean = false
)
