package com.ang.acb.todolearn.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.FakeTasksRepository
import com.ang.acb.todolearn.util.Event
import com.ang.acb.todolearn.TestCoroutineRule
import com.ang.acb.todolearn.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TaskDetailsViewModelTest {

    // Subject under test
    private lateinit var viewModel: TaskDetailsViewModel

    // A fake repository that will be injected in the view model during tests
    private lateinit var fakeTasksRepository: FakeTasksRepository

    // Swaps the background executor used by the Architecture Components
    // with a different one which executes each task synchronously.
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Sets the main coroutines dispatcher to a TestCoroutineDispatcher
    // with a TestCoroutineScope.
    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    private val task1 = Task("Title1", "Description1")
    private val task2 = Task("Title2", "Description2", true)
    private val task3 = Task("Title3", "Description3", true)

    @Before
    fun setUp() {
        fakeTasksRepository = FakeTasksRepository()
        fakeTasksRepository.addTasks(task1, task2, task3)
        viewModel = TaskDetailsViewModel(fakeTasksRepository)
    }

    @Test
    fun editTask_triggersEditTaskEvent() {
        // Given a repository containing an active task
        val task = Task("Title", "Description")
        fakeTasksRepository.addTasks(task)

        // When editing a task
        viewModel.editTaskEvent(task.id)

        // Then the edit task event is triggered
        val value = viewModel.editTaskEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun completeTask_dataAndSnackbarUpdated() {
        // Given a repository containing an active task
        val task = Task("Title", "Description", false)
        fakeTasksRepository.addTasks(task)

        // When an active task is marked as completed
        viewModel.completeTask(task, true)

        // Then the actual task is updated
        assertThat(fakeTasksRepository.tasksMap[task.id]?.isCompleted, `is`(true))

        // And the snackbar text is updated
        val snackbarText: Event<Int> =  viewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_completed_message))
    }

    @Test
    fun activateTask_dataAndSnackbarUpdated() {
        // Given a repository containing a completed task
        val task = Task("Title", "Description", true)
        fakeTasksRepository.addTasks(task)

        // When a completed task is marked as active
        viewModel.start(task.id)
        viewModel.completeTask(task, false)

        // Then the actual task is updated
        assertThat(fakeTasksRepository.tasksMap[task.id]?.isCompleted, `is`(false))

        // And the snackbar text is updated
        val snackbarText: Event<Int> =  viewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_active_message))
    }

    @Test
    fun deleteTask_dataAndEventUpdated() {
        // Given a repository containing a completed task
        val task = Task("Title", "Description")
        fakeTasksRepository.addTasks(task)

        // When deleting the task
        viewModel.start(task.id)
        viewModel.deleteTask()

        // Then the task is deleted and tasks data updated
        assertThat(fakeTasksRepository.tasksMap.keys.contains(task.id), `is`(false))

        // Then the delete task event is triggered
        val value = viewModel.deleteTaskEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }
}