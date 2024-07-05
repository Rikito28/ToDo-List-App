package com.dicoding.todoapp.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
//TODO 2 : Define data access object (DAO)
interface TaskDao {

    @RawQuery([Task::class])
    fun getTasks(query: SupportSQLiteQuery): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM tasks WHERE tasks.id = :taskId")
    fun getTaskById(taskId: Int): LiveData<Task>

    @Query("SELECT * FROM tasks WHERE completed = 0 ORDER BY dueDateMillis ASC")
    fun getNearestActiveTask(): Task

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task): Long

    @Insert
    fun insertAll(vararg tasks: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("UPDATE tasks SET completed = :completed WHERE id = :taskId")
    suspend fun updateCompleted(taskId: Int, completed: Boolean)
    
}
