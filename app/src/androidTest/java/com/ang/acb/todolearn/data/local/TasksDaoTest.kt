package com.ang.acb.todolearn.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ang.acb.todolearn.data.util.TestUtils
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

@SmallTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TasksDaoTest {

    // The subject under test
    private lateinit var database : TasksDatabase

    // Swaps the background executor used by the Architecture Components with a
    // different one which executes each task synchronously.
    @get: Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        // Create an in-memory version of the database to make tests more hermetic
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TasksDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertTaskAndGetById() = runBlockingTest {
        // GIVEN - insert a task
        val task = Task("title", "description")
        database.tasksDao().insert(task)

        // WHEN - get the task by id from the database
        val loaded = database.tasksDao().getTaskById(task.id)

        // THEN - the loaded data contains the expected values
        assertThat(loaded as Task, notNullValue())
        assertThat(loaded.id, `is`(task.id))
        assertThat(loaded.title, `is`(task.title))
        assertThat(loaded.description, `is`(task.description))
        assertThat(loaded.isCompleted, `is`(task.isCompleted))
    }

    @Test
    fun updateTaskAndGetById() = runBlockingTest {
        // GIVEN - insert a task
        val originalTask = Task("title", "description")
        database.tasksDao().insert(originalTask)

        // WHEN - the task is updated
        val updatedTask = Task("new title", "new description", true, originalTask.id)
        database.tasksDao().update(updatedTask)

        // THEN - the loaded data contains the expected values
        val loaded = database.tasksDao().getTaskById(originalTask.id)
        assertThat(loaded?.id, `is`(originalTask.id))
        assertThat(loaded?.title, `is`("new title"))
        assertThat(loaded?.description, `is`("new description"))
        assertThat(loaded?.isCompleted, `is`(true))
    }

    @Test
    fun insertTaskAndComplete() = runBlockingTest {
        // GIVEN - insert a task
        val task = Task("title", "description")
        database.tasksDao().insert(task)

        // WHEN - the task is updated
        database.tasksDao().updateCompleted(taskId = task.id, isCompleted = true)

        // THEN - the loaded data contains the expected values
        val loaded = database.tasksDao().getTaskById(task.id)
        assertThat(loaded?.id, `is`(task.id))
        assertThat(loaded?.title, `is`("title"))
        assertThat(loaded?.description, `is`("description"))
        assertThat(loaded?.isCompleted, `is`(true))
    }

    @Test
    fun insertTasksAndLoadAll() = runBlockingTest {
        // GIVEN - insert 50 tasks
        val tasks = TestUtils.createTasks(50)

        database.tasksDao().insertAll(tasks)

        // WHEN - get the tasks from the database
        val loaded = database.tasksDao().getTasks()

        // THEN - the loaded data contains the expected values
        assertThat(loaded as List<Task>, notNullValue())
        assertThat(loaded.size, `is` (50))
    }

    @Test
    fun insertTasksAndDeleteOne() = runBlockingTest {
        // GIVEN - insert 2 tasks
        val tasks = TestUtils.createTasks(2)
        database.tasksDao().insertAll(tasks)

        // WHEN - one task is deleted
        database.tasksDao().delete(tasks[0])

        // THEN - the loaded data contains the expected values
        val loaded = database.tasksDao().getTasks()
        assertThat(loaded as List<Task>, notNullValue())
        assertThat(loaded.size, `is` (1))
    }

    @Test
    fun insertTasksAndDeleteAll() = runBlockingTest {
        // GIVEN - insert 10 tasks
        val tasks = TestUtils.createTasks(10)
        database.tasksDao().insertAll(tasks)

        // WHEN - all tasks are deleted
        database.tasksDao().deleteAllTasks()

        // THEN - the loaded data contains the expected values
        val loaded = database.tasksDao().getTasks()
        assertThat(loaded as List<Task>, notNullValue())
        assertThat(loaded.size, `is` (0))
    }

    @Test
    fun insertTasksAndDeleteCompleted() = runBlockingTest {
        // GIVEN - insert 15 active tasks and 10 completed tasks
        val active = TestUtils.createActiveTasks(15)
        val completed = TestUtils.createCompletedTasks(10)
        database.tasksDao().insertAll(active)
        database.tasksDao().insertAll(completed)

        // WHEN - completed tasks are deleted
        database.tasksDao().deleteCompletedTasks()

        // THEN - the loaded data contains the expected values
        val loaded = database.tasksDao().getTasks()
        assertThat(loaded as List<Task>, notNullValue())
        assertThat(loaded.size, `is` (15))
    }
}