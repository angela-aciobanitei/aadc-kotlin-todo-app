package com.ang.acb.todolearn.ui.list

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.repo.TasksRepository
import com.ang.acb.todolearn.receiver.AlarmReceiver
import com.ang.acb.todolearn.ui.common.ADD_EDIT_RESULT_OK
import com.ang.acb.todolearn.ui.common.DELETE_RESULT_OK
import com.ang.acb.todolearn.util.Event
import com.ang.acb.todolearn.util.cancelNotifications
import kotlinx.coroutines.launch
import kotlin.random.Random


private const val ALARM_REQUEST_CODE = 0

/**
 * The [ViewModel] for the [TasksFragment]
 */
class TasksViewModel(
    private val app: Application,
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _newTaskEvent = MutableLiveData<Event<Unit>>()
    val newTaskEvent: LiveData<Event<Unit>> = _newTaskEvent

    private val _openTaskDetails = MutableLiveData<Event<String>>()
    val openTaskDetails: LiveData<Event<String>> = _openTaskDetails

    private val _openSettingsEvent = MutableLiveData<Event<Unit>>()
    val openSettingsEvent: LiveData<Event<Unit>> = _openSettingsEvent

    private val _currentFilter = MutableLiveData<Int>()
    val currentFilter: LiveData<Int> = _currentFilter

    private var resultMessageShown: Boolean = false
    private var alarmOn : Boolean = false
    private val testingTime = 10_000L // seconds

    private val allTasks : LiveData<PagedList<Task>> = tasksRepository.getAllPagedTasks()
    private val activeTasks = tasksRepository.getActivePagedTasks()
    private val completedTasks = tasksRepository.getCompletedPagedTasks()

    val tasks = _currentFilter.switchMap(::filterTasks)

    val empty: LiveData<Boolean> = tasks.map { it.isNullOrEmpty() }

    private val alarmManager = app.getSystemService(
        Context.ALARM_SERVICE) as AlarmManager

    private val notificationManager =  ContextCompat.getSystemService(
        app, NotificationManager::class.java) as NotificationManager

    // An explicit intent for the AlarmReceiver.
    private val notifyIntent = Intent(app.applicationContext, AlarmReceiver::class.java)

    // A PendingIntent for the Broadcast Receiver that handles notifications.
    private val notifyPendingIntent: PendingIntent by lazy {
        PendingIntent.getBroadcast(
            app.applicationContext,
            ALARM_REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    init {
        // Set initial state
        _currentFilter.value = TasksFilter.ALL_TASKS.value

        alarmOn = PendingIntent.getBroadcast(
            app,
            ALARM_REQUEST_CODE,
            notifyIntent,
            // Flag indicating that if the described PendingIntent does not
            // already exist, then simply return null instead of creating it.
            PendingIntent.FLAG_NO_CREATE
        ) != null

        // TODO: REMOVE THIS, DEBUG ONLY
        insertTestData()
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

    fun setAlarm(isOn: Boolean) {
        when (isOn) {
            true -> fireAlarm()
            false -> cancelAlarm()
        }
    }

    /**
     * Creates a new alarm to schedule a notification.
     */
    private fun fireAlarm() {
        if (!alarmOn) {
            // Now the switcher is on.
            alarmOn = true

            // Cancel previous notifications.
            notificationManager.cancelNotifications()

            // Determine when to trigger the alarm.
            val triggerTime = SystemClock.elapsedRealtime() + testingTime

            // Schedule an alarm to be delivered precisely at the stated time. This alarm
            // will be allowed to execute even when the system is in low-power idle modes.
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                // Because we used SystemClock.elapsedRealtime()
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                notifyPendingIntent
            )
        }
    }

    private fun cancelAlarm() {
        // Reset the alarm ON flag to false.
        alarmOn = false
        alarmManager.cancel(notifyPendingIntent)
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


@Suppress("UNCHECKED_CAST")
class TasksViewModelFactory(
    private val app : Application,
    private val tasksRepository: TasksRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            return TasksViewModel(app, tasksRepository) as T
        }
        throw IllegalArgumentException("Unable to construct view model")
    }
}
