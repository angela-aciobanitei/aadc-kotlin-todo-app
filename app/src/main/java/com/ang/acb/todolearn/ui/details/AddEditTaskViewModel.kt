package com.ang.acb.todolearn.ui.details

import androidx.lifecycle.*
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.Result
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.repo.ITasksRepository
import com.ang.acb.todolearn.util.Event
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * The [ViewModel] for the  [AddEditTaskFragment]
 */
class AddEditTaskViewModel(private val tasksRepository: ITasksRepository) : ViewModel() {

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    private var taskId: String? = null
    private var isNewTask: Boolean = false
    private var isCompleted = false

    private val simpleDateFormat = SimpleDateFormat("MMM d Y, h:mm a", Locale.getDefault())

    private val _taskUpdatedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent: LiveData<Event<Unit>> = _taskUpdatedEvent

    private val _taskDeadlineEvent = MutableLiveData<Event<Unit>>()
    val taskDeadlineEvent: LiveData<Event<Unit>> = _taskDeadlineEvent

    private val _deadline = MutableLiveData<Long>()
    val deadline: LiveData<String> = _deadline.map {
        simpleDateFormat.format(_deadline.value)
    }

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    fun start(id: String?) {
       taskId = id
       if (id == null) {
           // This is a new task, no need to populate
           isNewTask = true
           return
       }

       // If task ID is not null, this is a task edit
       isNewTask = false

       viewModelScope.launch {
           tasksRepository.getTask(id).let { result ->
               if (result is Result.Success) {
                   title.value = result.data.title
                   description.value = result.data.description
                   isCompleted = result.data.isCompleted
                   _deadline.value = result.data.deadline
               } else {
                   _snackbarText.value = Event(R.string.error_loading_task_message)
               }
           }
       }
    }

    fun addTaskDeadlineEvent() {
        _taskDeadlineEvent.value = Event(Unit)
    }

    fun setDeadline(hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        _deadline.value = calendar.timeInMillis
    }

    /**
     * Called by Data Binding when the Save Task FloatingActionButton is clicked.
     */
    fun saveTask(){
        val currentTitle = title.value
        val currentDescription = description.value
        val currentDeadline = _deadline.value
        val currentId = taskId

        if (currentTitle == null || currentDescription == null) {
            _snackbarText.value = Event(R.string.empty_task_message)
            return
        }

        if (currentTitle.isEmpty() || currentDescription.isEmpty()) {
            _snackbarText.value = Event(R.string.empty_task_message)
            return
        }

        if (isNewTask || currentId == null) {
            createTask(Task(
                title = currentTitle,
                description = currentDescription,
                deadline = currentDeadline ?: 0L
            ))
        } else {
             updateTask(Task(
                    title = currentTitle,
                    description = currentDescription,
                    isCompleted = isCompleted,
                    deadline = currentDeadline ?: 0L,
                    id = currentId
                ))
        }
    }

    fun createTask(task: Task) =  viewModelScope.launch {
        tasksRepository.saveTask(task)
        _snackbarText.value = Event(R.string.saved_task_message)
        _taskUpdatedEvent.value = Event(Unit)
    }

    fun updateTask(task: Task) {
        if (isNewTask) throw RuntimeException("Task is new, cannot call updateTask().")

        viewModelScope.launch {
            tasksRepository.updateTask(task)
            _snackbarText.value = Event(R.string.saved_task_message)
            _taskUpdatedEvent.value = Event(Unit)
        }
    }
}

