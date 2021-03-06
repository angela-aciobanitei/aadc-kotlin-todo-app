package com.ang.acb.todolearn.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.fakes.FakeTasksRepository
import com.ang.acb.todolearn.util.Event
import com.ang.acb.todolearn.utils.TestCoroutineRule
import com.ang.acb.todolearn.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class TasksViewModelTest {

    // Subject under test
    private lateinit var tasksViewModel: TasksViewModel

    // A fake repository that will be injected in the view model during tests
    private lateinit var fakeTasksRepository: FakeTasksRepository

    // Swaps the background executor used by the Architecture Components with a
    // different one which executes each task synchronously.
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Sets the main coroutines dispatcher to a TestCoroutineDispatcher with a TestCoroutineScope.
    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
        fakeTasksRepository = FakeTasksRepository()
        val task1 = Task(title = "Title1", description = "Description1")
        val task2 = Task(title = "Title2", description = "Description2", isCompleted = true)
        val task3 = Task(title = "Title3", description = "Description3", isCompleted = true)
        fakeTasksRepository.addTasks(task1, task2, task3)

        tasksViewModel = TasksViewModel(fakeTasksRepository)
    }

    @Test
    fun addNewTask_setsNewTaskEvent() {
        // When adding a new task
        tasksViewModel.addNewTaskEvent()

        // Then the new task event is triggered
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun completeTask_dataAndSnackbarUpdated() {
        // Given a repository containing an active task
        val task = Task(title = "Title", description = "Description", isCompleted = false)
        fakeTasksRepository.addTasks(task)

        // When an active task is marked as completed
        tasksViewModel.completeTask(task, true)

        // Then the actual task is updated
        assertThat(fakeTasksRepository.tasksMap[task.id]?.isCompleted, `is`(true))

        // And the snackbar text is updated
        val snackbarText: Event<Int> =  tasksViewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_completed_message))
    }

    @Test
    fun activateTask_dataAndSnackbarUpdated() {
        // Given a repository containing a completed task
        val task = Task(title = "Title", description = "Description", isCompleted = true)
        fakeTasksRepository.addTasks(task)

        // When a completed task is marked as active
        tasksViewModel.completeTask(task, false)

        // Then the actual task is updated
        assertThat(fakeTasksRepository.tasksMap[task.id]?.isCompleted, `is`(false))

        // And the snackbar text is updated
        val snackbarText: Event<Int> =  tasksViewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_active_message))
    }

    @Test
    fun clearAllTasks_dataAndSnackbarUpdated() = mainCoroutineRule.runBlockingTest {
        // When all tasks are cleared
        tasksViewModel.clearAllTasks()

        // Then the task data is cleared
        assertThat(fakeTasksRepository.tasksMap.isEmpty(), `is`(true))

        // And the snackbar text is updated
        val snackbarText: Event<Int> =  tasksViewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.tasks_cleared_message))
    }

    @Test
    fun clearCompletedTasks_dataAndSnackbarUpdated() = mainCoroutineRule.runBlockingTest {
        // When completed tasks are cleared
        tasksViewModel.clearCompletedTasks()

        // Then the task data is cleared
        assertThat(fakeTasksRepository.tasksMap.isEmpty(), `is`(false))
        assertThat(fakeTasksRepository.tasksMap.keys.size, `is`(1))

        // And the snackbar text is updated
        val snackbarText: Event<Int> =  tasksViewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.completed_tasks_cleared_message))
    }
}