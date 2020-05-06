package com.ang.acb.todolearn.data.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*


/**
 * Interface for database access on [Task] related operations.
 */
@Dao
interface TasksDao {

    @Query("SELECT * FROM tasks")
    fun getLiveTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks")
    fun getAllPagedTasks(): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM tasks WHERE completed = 1")
    fun getCompletedPagedTasks(): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM tasks WHERE completed = 0")
    fun getActivePagedTasks(): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getLiveTaskById(taskId: String): LiveData<Task>

    @Query("SELECT * FROM tasks")
    suspend fun getTasks(): List<Task>?

    @Query("SELECT * FROM tasks WHERE completed = 1")
    suspend fun getCompletedTasks(): List<Task>?

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: String): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<Task>)

    @Update
    suspend fun update(task: Task)

    @Query("UPDATE tasks SET completed = :isCompleted WHERE id = :taskId")
    suspend fun updateCompleted(taskId: String, isCompleted: Boolean)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: String)

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks(): Int

    @Query("DELETE FROM tasks WHERE completed = 1")
    suspend fun deleteCompletedTasks(): Int
}