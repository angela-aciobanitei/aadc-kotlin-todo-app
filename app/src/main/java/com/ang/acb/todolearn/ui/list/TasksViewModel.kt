package com.ang.acb.todolearn.ui.list

import androidx.lifecycle.*
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.Result
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.repo.TasksRepository
import com.ang.acb.todolearn.ui.common.ADD_EDIT_RESULT_OK
import com.ang.acb.todolearn.ui.common.DELETE_RESULT_OK
import com.ang.acb.todolearn.util.Event
import kotlinx.coroutines.launch

/**
 * The [ViewModel] for the [TasksFragment]
 */
class TasksViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private var resultMessageShown: Boolean = false

    private val _newTaskEvent = MutableLiveData<Event<Unit>>()
    val newTaskEvent: LiveData<Event<Unit>> = _newTaskEvent

    private val _openTaskDetails = MutableLiveData<Event<String>>()
    val openTaskDetails: LiveData<Event<String>> = _openTaskDetails

    private val _openSettingsEvent = MutableLiveData<Event<Unit>>()
    val openSettingsEvent: LiveData<Event<Unit>> = _openSettingsEvent

    private val _currentFilter = MutableLiveData<Int>()
    val currentFilter: LiveData<Int> = _currentFilter

    private val allTasks = tasksRepository.getLiveTasks().map { resultTask ->
        if (resultTask is Result.Success) {
            resultTask.data
        } else {
            _snackbarText.value = Event(R.string.error_loading_task_message)
            emptyList()
        }
    }

    private val activeTasks = allTasks.map { taskList ->
        taskList.filter { !it.isCompleted }
    }

    private val completedTasks = allTasks.map { taskList ->
        taskList.filter { it.isCompleted }
    }

    val tasks = _currentFilter.switchMap {
        getFilteredTasks(it)
    }

    init {
        // Set initial state
        _currentFilter.value = TasksFilter.ALL_TASKS.value
    }

    fun updateFilter(filter: TasksFilter) {
        _currentFilter.value = filter.value
    }

    private fun getFilteredTasks(filter: Int) :  LiveData<List<Task>>{
        return when (filter) {
            TasksFilter.ACTIVE_TASKS.value -> activeTasks
            TasksFilter.COMPLETED_TASKS.value -> completedTasks
            else -> allTasks
        }
    }

    /**
     * Called by Data Binding in the tasks_fragment.xml layout
     * when the Add New Task FloatingActionButton is clicked.
     */
    fun navigateToAddTask() {
        _newTaskEvent.value = Event(Unit)
    }

    /**
     * Called by Data Binding in the item_task.xml layout when
     * a Task item is clicked.
     */
    fun navigateToTaskDetails(id: String) {
        _openTaskDetails.value = Event(id)
    }

    fun navigateToSettingsScreen() {
        _openSettingsEvent.value = Event(Unit)
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
            ADD_EDIT_RESULT_OK -> _snackbarText.value = Event(R.string.saved_task_message)
            DELETE_RESULT_OK -> _snackbarText.value = Event(R.string.deleted_task_message)
        }

        resultMessageShown = true
    }

    fun clearAllTasks() {
        viewModelScope.launch {
            tasksRepository.deleteAllTasks()
            _snackbarText.value = Event(R.string.tasks_cleared_message)
        }
    }

    fun clearCompletedTasks() {
        viewModelScope.launch {
            tasksRepository.deleteCompletedTasks()
            _snackbarText.value = Event(R.string.completed_tasks_cleared_message)
        }
    }
}
