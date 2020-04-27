package com.ang.acb.todolearn.data.local

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

interface TasksDataSource {
    suspend fun saveTask(task: Task)

    suspend fun saveTasks(tasks: List<Task>)

    suspend fun updateTask(task: Task)

    suspend fun activateTask(task: Task)

    suspend fun completeTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun deleteTaskById(taskId: String)

    suspend fun deleteAllTasks()

    suspend fun deleteCompletedTasks()

    suspend fun getTask(taskId: String): Result<Task>

    suspend fun getTasks(): Result<List<Task>>
    fun getLiveTask(taskId: String): LiveData<Result<Task>>
    fun getLiveTasks(): LiveData<Result<List<Task>>>
    fun getAllPagedTasks(): LiveData<PagedList<Task>>
    fun getCompletedPagedTasks(): LiveData<PagedList<Task>>
    fun getActivePagedTasks(): LiveData<PagedList<Task>>
}