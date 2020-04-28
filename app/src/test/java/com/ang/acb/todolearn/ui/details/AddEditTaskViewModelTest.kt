package com.ang.acb.todolearn.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.repo.FakeTasksRepository
import com.ang.acb.todolearn.util.Event
import com.ang.acb.todolearn.util.MainCoroutineRule
import com.ang.acb.todolearn.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditTaskViewModelTest {

    //Subject under test
    private lateinit var viewModel: AddEditTaskViewModel

    // A fake repository that will be injected in the view model during tests
    private lateinit var fakeTasksRepository: FakeTasksRepository

    // Swaps the background executor used by the Architecture Components with a
    // different one which executes each task synchronously.
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Sets the main coroutines dispatcher to a TestCoroutineDispatcher with a TestCoroutineScope.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val task1 = Task("Title1", "Description1")
    private val task2 = Task("Title2", "Description2", true)
    private val task3 = Task("Title3", "Description3", true)

    @Before
    fun setUp() {
        fakeTasksRepository = FakeTasksRepository()
        fakeTasksRepository.addTasks(task1, task2, task3)
        viewModel = AddEditTaskViewModel(fakeTasksRepository)
    }

    @Test
    fun addNewTask_savesTaskUpdatesSnackbarAndEvent() {
        // When creating a new task
        val newTask = Task("new title", "new description")
        viewModel.createTask(newTask)

        // Then the new task is actually saved via the repository
        assertThat(fakeTasksRepository.tasksMap.values.contains(newTask), `is`(true))

        // Then the snackbar text is updated
        val snackbarText: Event<Int> =  viewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.saved_task_message))

        // Then the task updated event is triggered
        val value = viewModel.taskUpdatedEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun updateExistingTask_updatesTaskAndSnackbarAndEvent() {
        // When updating an existing task
        val updated = task1.copy(
            title = "new title",
            description = "new description",
            isCompleted = true
        )
        viewModel.updateTask(updated)

        // Then the task is actually updated
        val loaded = fakeTasksRepository.tasksMap[task1.id]
        assertThat(loaded, `is`(notNullValue()))
        assertThat(loaded?.id, `is`(updated.id))
        assertThat(loaded?.title, `is`(updated.title))
        assertThat(loaded?.description, `is`(updated.description))
        assertThat(loaded?.isCompleted, `is`(updated.isCompleted))

        // Then the snackbar text is updated
        val snackbarText: Event<Int> =  viewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.saved_task_message))

        // Then the task updated event is triggered
        val value = viewModel.taskUpdatedEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }
}