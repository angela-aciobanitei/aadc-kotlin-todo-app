package com.ang.acb.todolearn.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.ang.acb.todolearn.TestCoroutineRule
import com.ang.acb.todolearn.PojoTestUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Integration test for [TasksLocalDataSource] and [TasksDatabase]
 */
@MediumTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TasksLocalDataSourceTest {

    // Subjects under test
    private lateinit var localDataSource: TasksLocalDataSource
    private lateinit var database: TasksDatabase

    // Swaps the background executor used by the Architecture Components
    // with a different one which executes each task synchronously.
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Sets the main coroutines dispatcher to a TestCoroutineDispatcher
    // with a TestCoroutineScope.
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @Before
    fun initDb() {
        // Create an in-memory version of the database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TasksDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource = TasksLocalDataSource(
            database.tasksDao(),
            Dispatchers.Main
        )
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertTaskAndGetById() = testCoroutineRule.runBlockingTest {
        // GIVEN - insert a new task
        val task = Task("title", "description")
        localDataSource.saveTask(task)

        // WHEN - task retrieved by ID
        val loaded = localDataSource.getTask(task.id)

        // THEN - the loaded data contains the expected values
        assertThat(loaded as Result.Success, notNullValue())
        assertThat(loaded.data.id, `is`(task.id))
        assertThat(loaded.data.title, `is`(task.title))
        assertThat(loaded.data.description, `is`(task.description))
        assertThat(loaded.data.isCompleted, `is`(task.isCompleted))
    }

    @Test
    fun updateTaskAndGetById() = testCoroutineRule.runBlockingTest {
        // GIVEN - insert a task
        val originalTask = Task("title", "description")
        localDataSource.saveTask(originalTask)

        // WHEN - the task is updated
        val updatedTask = Task("new title", "new description", true, originalTask.id)
        localDataSource.updateTask(updatedTask)

        // THEN - the loaded data contains the expected values
        val loaded = localDataSource.getTask(originalTask.id)
        assertThat(loaded as Result.Success, notNullValue())
        assertThat(loaded.data.id, `is`(originalTask.id))
        assertThat(loaded.data.title, `is`("new title"))
        assertThat(loaded.data.description, `is`("new description"))
        assertThat(loaded.data.isCompleted, `is`(true))
    }

    @Test
    fun insertTaskAndComplete() = testCoroutineRule.runBlockingTest {
        // GIVEN - insert an active task
        val task = Task("title", "description")
        localDataSource.saveTask(task)

        // WHEN - the task is updated as completed
        localDataSource.completeTask(task)

        // THEN - the loaded data contains the expected values
        val loaded = localDataSource.getTask(task.id)
        assertThat(loaded as Result.Success, notNullValue())
        assertThat(loaded.data.id, `is`(task.id))
        assertThat(loaded.data.title, `is`("title"))
        assertThat(loaded.data.description, `is`("description"))
        assertThat(loaded.data.isCompleted, `is`(true))
    }

    @Test
    fun insertTasksAndLoadAll() = testCoroutineRule.runBlockingTest {
        // GIVEN - insert 10 tasks
        val tasks = PojoTestUtils.createTasks(10)

        localDataSource.saveTasks(tasks)

        // WHEN - get the tasks from the database
        val loaded = localDataSource.getTasks()

        // THEN - the loaded data contains the expected values
        assertThat(loaded as Result.Success, notNullValue())
        assertThat(loaded.data.size, `is` (10))
    }

    @Test
    fun insertTasksAndDeleteOne() = testCoroutineRule.runBlockingTest {
        // GIVEN - insert 2 tasks
        val tasks = PojoTestUtils.createTasks(2)
        localDataSource.saveTasks(tasks)

        // WHEN - one task is deleted
        localDataSource.deleteTask(tasks[0])

        // THEN - the loaded data contains the expected values
        val loaded = localDataSource.getTasks()
        assertThat(loaded as Result.Success, notNullValue())
        assertThat(loaded.data.size, `is` (1))
    }

    @Test
    fun insertTasksAndDeleteAll() = testCoroutineRule.runBlockingTest {
        // GIVEN - insert 10 tasks
        val tasks = PojoTestUtils.createTasks(10)
        localDataSource.saveTasks(tasks)

        // WHEN - all tasks are deleted
        localDataSource.deleteAllTasks()

        // THEN - the loaded data contains the expected values
        val loaded = localDataSource.getTasks()
        assertThat(loaded as Result.Success, notNullValue())
        assertThat(loaded.data.size, `is` (0))
    }

    @Test
    fun insertTasksAndDeleteCompleted() = testCoroutineRule.runBlockingTest {
        // GIVEN - insert 15 active tasks and 10 completed tasks
        val active = PojoTestUtils.createActiveTasks(15)
        val completed = PojoTestUtils.createCompletedTasks(10)
        localDataSource.saveTasks(active)
        localDataSource.saveTasks(completed)

        // WHEN - completed tasks are deleted
        localDataSource.deleteCompletedTasks()

        // THEN - the loaded data contains the expected values
        val loaded = localDataSource.getTasks()
        assertThat(loaded as Result.Success, notNullValue())
        assertThat(loaded.data.size, `is` (15))
        assertThat(loaded.data.containsAll(active), `is` (true))
    }
}