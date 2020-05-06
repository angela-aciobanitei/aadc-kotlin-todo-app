package com.ang.acb.todolearn.data.repo

import com.ang.acb.todolearn.data.local.Result
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.fakes.FakeDataSource
import com.ang.acb.todolearn.utils.PojoTestUtils
import com.ang.acb.todolearn.utils.TestCoroutineRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class TasksRepositoryTest {

    // Subject under test
    private lateinit var tasksRepository: TasksRepository
    private lateinit var tasksDataSource: FakeDataSource

    private val task1 = Task(title = "Title1", description = "Description1")
    private val task2 = Task(title = "Title2", description = "Description2")
    private val task3 = Task(title = "Title3", description = "Description3")
    private val task4 = Task(title = "Title4", description = "Description4", isCompleted = true)
    private val task5 = Task(title = "Title5", description = "Description5", isCompleted = true)
    private val task6 = Task(title = "Title6", description = "Description6", isCompleted = true)
    private val activeTasks :List<Task> = listOf(task1, task2, task3).sortedBy { it.id }
    private val completedTasks : List<Task> = listOf(task4, task5, task6).sortedBy { it.id }
    private val localTasks : List<Task> = activeTasks + completedTasks

    // Sets the main coroutines dispatcher to a TestCoroutineDispatcher with a TestCoroutineScope.
    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    @Before
    fun initRepo() {
        tasksDataSource =
            FakeDataSource(localTasks.toMutableList())
        tasksRepository = TasksRepository(tasksDataSource, Dispatchers.Main)
    }


    @Test
    fun saveTask_getTasks() = mainCoroutineRule.runBlockingTest {
        // GIVEN - save a task
        val task = Task(title = "Title7", description = "Description7")
        tasksRepository.saveTask(task)

        // WHEN - tasks are requested from the tasks repository
        val loaded = tasksRepository.getTasks() as Result.Success

        // THEN - the loaded data contains the expected values
        assertThat(loaded.data, notNullValue())
        assertThat(loaded.data.contains(task), `is`(true))
    }

    @Test
    fun saveTasks_getTasks() = mainCoroutineRule.runBlockingTest {
        // GIVEN - save 3 tasks
        val tasks = PojoTestUtils.createTasks(3)
        tasksRepository.saveTasks(tasks)

        // WHEN - tasks are requested from the tasks repository
        val loaded = tasksRepository.getTasks() as Result.Success

        // THEN - the loaded data contains the expected values
        assertThat(loaded.data, notNullValue())
        assertThat(loaded.data.containsAll(tasks), `is`(true))
    }

    @Test
    fun deleteTask_getTasks() = mainCoroutineRule.runBlockingTest {
        // GIVEN - delete a task
        tasksRepository.deleteTask(task3)

        // WHEN - tasks are requested from the tasks repository
        val loaded = tasksRepository.getTasks() as Result.Success

        // THEN - the loaded data contains the expected values
        assertThat(loaded.data, notNullValue())
        assertThat(loaded.data.contains(task3), `is`(false))
    }

    @Test
    fun deleteAllTasks_getTasks() = mainCoroutineRule.runBlockingTest {
        // GIVEN - delete all tasks
        tasksRepository.deleteAllTasks()

        // WHEN - tasks are requested from the tasks repository
        val loaded = tasksRepository.getTasks() as Result.Success

        // THEN - the loaded data contains the expected values
        assertThat(loaded.data, notNullValue())
        assertThat(loaded.data.size, `is`(0))
    }

    @Test
    fun deleteCompletedTasks_getTasks() = mainCoroutineRule.runBlockingTest {
        // GIVEN - delete completed tasks
        tasksRepository.deleteCompletedTasks()

        // WHEN - tasks are requested from the tasks repository
        val loaded : Result.Success<List<Task>> = tasksRepository.getTasks() as Result.Success

        // THEN - the loaded data contains the expected values
        assertThat(loaded.data, notNullValue())
        assertThat(loaded.data.containsAll(activeTasks), `is`(true))
    }

    @Test
    fun updateTask_getTask() = mainCoroutineRule.runBlockingTest{
        // GIVEN - update a task
        val updated = Task(
            title = "new title",
            description = "new description",
            isCompleted = true,
            id = task1.id
        )
        tasksRepository.updateTask(updated)

        // WHEN - get the task by from the tasks repository
        val loaded = tasksRepository.getTask(task1.id) as Result.Success

        // THEN - the loaded data contains the expected values
        assertThat(loaded.data, notNullValue())
        assertThat(loaded.data.id, `is`(updated.id))
        assertThat(loaded.data.title, `is`(updated.title))
        assertThat(loaded.data.description, `is`(updated.description))
        assertThat(loaded.data.isCompleted, `is`(updated.isCompleted))
    }

    @Test
    fun activateTask_getTask() = mainCoroutineRule.runBlockingTest{
        // GIVEN - activate a task
        tasksRepository.activateTask(task6)

        // WHEN - get the task by from the tasks repository
        val loaded = tasksRepository.getTask(task6.id) as Result.Success

        // THEN - the loaded data contains the expected values
        assertThat(loaded.data, notNullValue())
        assertThat(loaded.data.isCompleted, `is`(false))
    }

    @Test
    fun completeTask_getTask() = mainCoroutineRule.runBlockingTest{
        // GIVEN - activate a task
        tasksRepository.completeTask(task2)

        // WHEN - get the task by from the tasks repository
        val loaded = tasksRepository.getTask(task2.id) as Result.Success

        // THEN - the loaded data contains the expected values
        assertThat(loaded.data, notNullValue())
        assertThat(loaded.data.isCompleted, `is`(true))
    }
}