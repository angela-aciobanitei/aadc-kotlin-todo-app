package com.ang.acb.todolearn.ui.list

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.repo.ITasksRepository
import com.ang.acb.todolearn.ui.common.ADD_EDIT_RESULT_OK
import com.ang.acb.todolearn.ui.common.DELETE_RESULT_OK
import com.ang.acb.todolearn.util.Event
import kotlinx.coroutines.launch
import kotlin.random.Random


private const val ALARM_REQUEST_CODE = 0

/**
 * The [ViewModel] for the [TasksFragment]
 */
class TasksViewModel(
    private val tasksRepository: ITasksRepository
) : ViewModel() {

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _newTaskEvent = MutableLiveData<Event<Unit>>()
    val newTaskEvent: LiveData<Event<Unit>> = _newTaskEvent

    private val _openTaskDetails = MutableLiveData<Event<String>>()
    val openTaskDetails: LiveData<Event<String>> = _openTaskDetails

    private val _currentFilter = MutableLiveData<Int>()
    val currentFilter: LiveData<Int> = _currentFilter

    private var resultMessageShown: Boolean = false

    private val allTasks : LiveData<PagedList<Task>> = tasksRepository.getAllPagedTasks()
    private val activeTasks : LiveData<PagedList<Task>>  = tasksRepository.getActivePagedTasks()
    private val completedTasks : LiveData<PagedList<Task>> = tasksRepository.getCompletedPagedTasks()

    val tasks : LiveData<PagedList<Task>> = _currentFilter.switchMap(::filterTasks)

    val empty: LiveData<Boolean> = tasks.map { it.isNullOrEmpty() }

    init {
        // Set initial state
        _currentFilter.value = TasksFilter.ALL_TASKS.value
    }

    fun updateFilter(filter: TasksFilter) {
        _currentFilter.value = filter.value
    }

    private fun filterTasks(filter: Int) :  LiveData<PagedList<Task>>{
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
    fun addNewTaskEvent() {
        _newTaskEvent.value = Event(Unit)
    }

    /**
     * Called by Data Binding in the item_task.xml layout when
     * a Task item is clicked.
     */
    fun openTaskDetailsEvent(id: String) {
        _openTaskDetails.value = Event(id)
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

    private fun insertTestData() {
        viewModelScope.launch {
            val tasks = (0 until 500).map {
                Task(
                    title = "title$it",
                    description = "description$it",
                    isCompleted = Random.nextBoolean(),
                    id = "id$it"
                )
            }

            tasksRepository.saveTasks(tasks)
        }
    }
}
