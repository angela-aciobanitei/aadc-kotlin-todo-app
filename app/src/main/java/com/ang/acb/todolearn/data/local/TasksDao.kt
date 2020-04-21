package com.ang.acb.todolearn.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TasksDao {

    @Query("SELECT * FROM tasks")
    fun getLiveTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getLiveTaskById(taskId: Int): LiveData<Task>

    @Query("SELECT * FROM tasks")
    suspend fun getTasks(): List<Task>?

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("UPDATE tasks SET completed = :isCompleted WHERE id = :taskId")
    suspend fun updateCompleted(taskId: Int, isCompleted: Boolean)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Int)

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks(): Int

    @Query("DELETE FROM tasks WHERE completed = 1")
    suspend fun deleteCompletedTasks(): Int
}