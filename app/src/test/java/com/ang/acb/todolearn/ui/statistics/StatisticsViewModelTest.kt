package com.ang.acb.todolearn.ui.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.FakeTasksRepository
import com.ang.acb.todolearn.MainCoroutineRule
import com.ang.acb.todolearn.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class StatisticsViewModelTest {

    // Subject under test
    private lateinit var viewModel: StatisticsViewModel

    // A fake repository that will be injected in the view model during tests
    private lateinit var fakeTasksRepository: FakeTasksRepository

    // Swaps the background executor used by the Architecture Components
    // with a different one which executes each task synchronously.
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Sets the main coroutines dispatcher to a TestCoroutineDispatcher
    // with a TestCoroutineScope.
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        // Initialise the repository with no tasks
        fakeTasksRepository = FakeTasksRepository()
        viewModel = StatisticsViewModel(fakeTasksRepository)
    }

    @Test
    fun empty_returnsZeroes() {
        assertThat(viewModel.empty.getOrAwaitValue(), `is`(true))
        assertThat(viewModel.activeTasksPercent.getOrAwaitValue(), `is`(0f))
        assertThat(viewModel.completedTasksPercent.getOrAwaitValue(), `is`(0f))
    }

    @Test
    fun noActive_completedTaskPercentIsHundred() {
        fakeTasksRepository.addTasks(Task("title", "desc", true))
        assertThat(viewModel.empty.getOrAwaitValue(), `is`(false))
        assertThat(viewModel.activeTasksPercent.getOrAwaitValue(), `is`(0f))
        assertThat(viewModel.completedTasksPercent.getOrAwaitValue(), `is`(100f))
    }

    @Test
    fun noCompleted_activeTaskPercentIsHundred() {
        fakeTasksRepository.addTasks(Task("title", "desc", false))
        assertThat(viewModel.empty.getOrAwaitValue(), `is`(false))
        assertThat(viewModel.activeTasksPercent.getOrAwaitValue(), `is`(100f))
        assertThat(viewModel.completedTasksPercent.getOrAwaitValue(), `is`(0f))
    }

    @Test
    fun activeAndCompleted() {
        // Given 3 completed tasks and 2 active tasks
        fakeTasksRepository.addTasks(
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = false),
            Task("title", "desc", isCompleted = false)
        )

        assertThat(viewModel.activeTasksPercent.getOrAwaitValue(), `is`(40f))
        assertThat(viewModel.completedTasksPercent.getOrAwaitValue(), `is`(60f))
    }
}