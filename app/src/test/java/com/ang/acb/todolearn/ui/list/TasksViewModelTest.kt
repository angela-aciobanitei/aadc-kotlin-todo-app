package com.ang.acb.todolearn.ui.list

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.repo.FakeTasksRepository
import com.ang.acb.todolearn.util.Event
import com.ang.acb.todolearn.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * http://robolectric.org/getting-started/
 * https://stackoverflow.com/questions/56821193/does-robolectric-require-java-9
 */

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class TasksViewModelTest {

    // Subject under test
    private lateinit var tasksViewModel: TasksViewModel

    // A fake repository that will be injected in the view model during tests
    private lateinit var fakeTasksRepository: FakeTasksRepository

    // Swaps the background executor used by the Architecture Components with a
    // different one which executes each task synchronously.
    @get: Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        fakeTasksRepository = FakeTasksRepository()
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2", true)
        val task3 = Task("Title3", "Description3", true)
        fakeTasksRepository.addTasks(task1, task2, task3)

        tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext(), fakeTasksRepository)
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
        val task = Task("Title", "Description")
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
        val task = Task("Title", "Description")
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
    fun clearAllTasks_dataAndSnackbarUpdated() = runBlockingTest {
        // When all tasks are cleared
        tasksViewModel.clearAllTasks()

        // Then the task data is cleared
        assertThat(fakeTasksRepository.tasksMap.isEmpty(), `is`(true))

        // And the snackbar text is updated
        val snackbarText: Event<Int> =  tasksViewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.tasks_cleared_message))
    }

    @Test
    fun clearCompletedTasks_dataAndSnackbarUpdated() = runBlockingTest {
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