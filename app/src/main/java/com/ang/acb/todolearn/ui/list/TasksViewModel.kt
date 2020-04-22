package com.ang.acb.todolearn.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.repo.TasksRepository
import com.ang.acb.todolearn.ui.common.ADD_RESULT_OK
import com.ang.acb.todolearn.ui.common.EDIT_RESULT_OK
import com.ang.acb.todolearn.util.Event
import kotlinx.coroutines.launch

/**
 * The [ViewModel] for the [TasksFragment]
 */
class TasksViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    val tasks = tasksRepository.getLiveTasks()

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private var resultMessageShown: Boolean = false

    private val _newTaskEvent = MutableLiveData<Event<Unit>>()
    val newTaskEvent: LiveData<Event<Unit>> = _newTaskEvent

    /**
     * Called by Data Binding in the tasks_fragment.xml layout
     * when the Add New Task FloatingActionButton is clicked.
     */
    fun navigateToAddTask() {
        _newTaskEvent.value = Event(Unit)
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

    fun getResultMessage(result: Int) {
        // Prevent showing snackbar message incorrectly
        if (resultMessageShown) return
        when (result) {
            EDIT_RESULT_OK ->  _snackbarText.value = Event(R.string.updated_task_message)
            ADD_RESULT_OK ->  _snackbarText.value = Event(R.string.created_new_task_message)
        }

        resultMessageShown = true
    }
}
