package com.ang.acb.todolearn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.paging.PagedList
import com.ang.acb.todolearn.data.local.Result
import com.ang.acb.todolearn.data.local.Result.Success
import com.ang.acb.todolearn.data.local.Result.Error
import com.ang.acb.todolearn.data.local.Result.Loading
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.repo.ITasksRepository
import kotlinx.coroutines.runBlocking

/**
 * A fake tasks repository that implements the same interface as the
 * real repository, [TasksRepository]. This way, we can swap the real
 * version with the fake version during tests. This fake repository
 * just needs to return realistic fake outputs, given some inputs.
 */
class FakeTasksRepository : ITasksRepository {

    // Use a LinkedHashMap<> to store the tasks, where the key is the task ID
    // (a String) and the value is the actual Task object. Note: unlike HashMap,
    // LinkedHashMap preserves the insertion order of its entries.
    var tasksMap = LinkedHashMap<String, Task>()

    private val liveTasks = MutableLiveData<Result<List<Task>>>()
    private val pagedTasks = MutableLiveData<PagedList<Task>>()

    override suspend fun saveTask(task: Task) {
        tasksMap[task.id] = task
    }

    override suspend fun saveTasks(tasks: List<Task>) {
        // https://kotlinlang.org/docs/reference/map-operations.html
        val pairs = tasks.map { task -> task.id to task }
        tasksMap.putAll(pairs)
    }

    override suspend fun updateTask(task: Task) {
        tasksMap[task.id] = task
        refreshLiveTasks()
    }

    override suspend fun activateTask(task: Task) {
        tasksMap[task.id] = task.copy(isCompleted = false)
        refreshLiveTasks()
    }

    override suspend fun completeTask(task: Task) {
        tasksMap[task.id] = task.copy(isCompleted = true)
        refreshLiveTasks()
    }

    override suspend fun deleteTask(task: Task) {
        tasksMap.remove(task.id, task)
        refreshLiveTasks()
    }

    override suspend fun deleteTaskById(taskId: String) {
        tasksMap.remove(taskId)
        refreshLiveTasks()
    }

    override suspend fun deleteAllTasks() {
        tasksMap.clear()
        refreshLiveTasks()
    }

    override suspend fun deleteCompletedTasks() {
        tasksMap = tasksMap.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Task>
        refreshLiveTasks()
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        tasksMap[taskId]?.let {
            return Success(it)
        }
        return Error(Exception("Could not find task"))
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return Success(tasksMap.values.toList())
    }

    private suspend fun refreshLiveTasks() {
        liveTasks.value = getTasks()
    }

    // Helper function used when testing
    fun addTasks(vararg tasks: Task) {
        for (task in tasks) {
            tasksMap[task.id] = task
        }
        runBlocking { refreshLiveTasks() }
    }

    override fun getLiveTask(taskId: String): LiveData<Result<Task>> {
        runBlocking { refreshLiveTasks() }
        return liveTasks.map { tasks ->
            when (tasks) {
                is Loading -> Loading
                is Error -> Error(tasks.exception)
                is Success -> {
                    val task = tasks.data.firstOrNull { it.id == taskId }
                        ?: return@map Error(Exception("Not found"))
                    Success(task)
                }
            }
        }
    }

    override fun getLiveTasks(): LiveData<Result<List<Task>>> {
        runBlocking { refreshLiveTasks() }
        return liveTasks
    }

    override fun getAllPagedTasks(): LiveData<PagedList<Task>> {
        return pagedTasks
    }

    override fun getCompletedPagedTasks(): LiveData<PagedList<Task>> {
        return pagedTasks
    }

    override fun getActivePagedTasks(): LiveData<PagedList<Task>> {
        return pagedTasks
    }
}