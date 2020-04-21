package com.ang.acb.todolearn.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.Result
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.local.TasksRepository
import com.ang.acb.todolearn.ui.list.INVALID_TASK_ID
import com.ang.acb.todolearn.util.Event
import kotlinx.coroutines.launch

/**
 * The [ViewModel] for the  [AddEditTaskFragment]
 */
class AddEditTaskViewModel(val tasksRepository: TasksRepository) : ViewModel() {

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    private var taskId: Int? = null
    private var isNewTask: Boolean = false
    private var isCompleted = false

    private val _taskUpdatedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent: LiveData<Event<Unit>> = _taskUpdatedEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText


    fun start(id: Int) {
       taskId = id
       if (taskId == INVALID_TASK_ID) {
           // This is a new task, no need to populate
           isNewTask = true
           return
       }

       // If task ID is valid, this is a task edit
       isNewTask = false

       viewModelScope.launch {
           tasksRepository.getTask(id).let { result ->
               if (result is Result.Success) {
                   title.value = result.data.title
                   description.value = result.data.description
                   isCompleted = result.data.isCompleted
               } else {
                   _snackbarText.value = Event(R.string.error_message)
               }
           }
       }
    }

    fun saveTask(){
        val currentTitle = title.value
        val currentDescription = description.value
        val currentId = taskId

        if(currentTitle == null || currentDescription == null) {
            _snackbarText.value = Event(R.string.empty_task_message)
            return
        }

        if(currentTitle.isEmpty() || currentDescription.isEmpty()) {
            _snackbarText.value = Event(R.string.empty_task_message)
            return
        }

        if (isNewTask || currentId == INVALID_TASK_ID) {
            // https://developer.android.com/reference/androidx/room/PrimaryKey#autoGenerate()
            createTask(Task(null, currentTitle, currentDescription))
        } else {
            updateTask(Task(currentId, currentTitle, currentDescription, isCompleted))
        }
    }

    fun createTask(task: Task) =  viewModelScope.launch {
        tasksRepository.saveTask(task)
        _taskUpdatedEvent.value = Event(Unit)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        tasksRepository.updateTask(task)
        _taskUpdatedEvent.value = Event(Unit)
    }
}

