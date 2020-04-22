package com.ang.acb.todolearn.ui.details

import androidx.lifecycle.*
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.Result
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.repo.TasksRepository
import com.ang.acb.todolearn.util.Event
import kotlinx.coroutines.launch

class TaskDetailsViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    private val _taskId = MutableLiveData<String>()

    private val _task = _taskId.switchMap { id ->
        tasksRepository.getLiveTask(id).map { resultTask ->
            if (resultTask is Result.Success) {
                resultTask.data
            } else {
                _snackbarText.value = Event(R.string.error_loading_task_message)
                null
            }
        }
    }
    val task: LiveData<Task?> = _task

    private val _editTaskEvent = MutableLiveData<Event<String>>()
    val editTaskEvent: LiveData<Event<String>> = _editTaskEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    /**
     * Called by Data Binding in the task_details_fragment.xml layout
     * when the Edit Task FloatingActionButton is clicked.
     */
    fun navigateToEditTask(taskId: String) {
        _editTaskEvent.value = Event(taskId)
    }

    fun start(id: String?) {
        _taskId.value = id
    }

    /**
     * Called by Data Binding in the item_task.xml layout
     * when the `task_item_completed` CheckBox is clicked.
     */
    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        if (completed) {
            tasksRepository.completeTask(task)
            _snackbarText.value = Event(R.string.task_marked_completed_message)
        } else {
            tasksRepository.activateTask(task)
            _snackbarText.value = Event(R.string.task_marked_active_message)
        }
    }
}


