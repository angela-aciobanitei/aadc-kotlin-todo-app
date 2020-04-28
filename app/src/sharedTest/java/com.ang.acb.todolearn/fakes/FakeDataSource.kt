package com.ang.acb.todolearn

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ang.acb.todolearn.data.local.Result
import com.ang.acb.todolearn.data.local.Result.Success
import com.ang.acb.todolearn.data.local.Result.Error
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.local.TasksDataSource

class FakeDataSource(var tasks: MutableList<Task>? = mutableListOf()) : TasksDataSource {

    override suspend fun saveTask(task: Task) {
        tasks?.add(task)
    }

    override suspend fun saveTasks(tasks: List<Task>) {
        this.tasks?.addAll(tasks)
    }

    override suspend fun updateTask(task: Task) {
        val index = tasks?.indexOfFirst { it.id == task.id}
        index?.let {
            tasks?.set(index, task)
        }
    }

    override suspend fun activateTask(task: Task) {
        updateTask(task.copy(isCompleted = false))
    }

    override suspend fun completeTask(task: Task) {
        updateTask(task.copy(isCompleted = true))
    }

    override suspend fun deleteTask(task: Task) {
        tasks?.remove(task)
    }

    override suspend fun deleteTaskById(taskId: String) {
        tasks?.removeIf {
            it.id == taskId
        }
    }

    override suspend fun deleteAllTasks() {
        tasks?.clear()
    }

    override suspend fun deleteCompletedTasks() {
        tasks?.removeIf {
            it.isCompleted
        }
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        val task = tasks?.firstOrNull { it.id == taskId }
        task?.let {
            return Success(it)
        }
        return Error(Exception("Task not found"))
    }

    override suspend fun getTasks(): Result<List<Task>> {
        tasks?.let {
            return Success(ArrayList(it))
        }
        return Error(Exception("Tasks not found"))
    }

    override fun getLiveTask(taskId: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override fun getLiveTasks(): LiveData<Result<List<Task>>> {
        TODO("Not yet implemented")
    }

    override fun getAllPagedTasks(): LiveData<PagedList<Task>> {
        TODO("Not yet implemented")
    }

    override fun getCompletedPagedTasks(): LiveData<PagedList<Task>> {
        TODO("Not yet implemented")
    }

    override fun getActivePagedTasks(): LiveData<PagedList<Task>> {
        TODO("Not yet implemented")
    }
}