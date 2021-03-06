package com.ang.acb.todolearn.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ang.acb.todolearn.utils.PojoTestUtils
import com.ang.acb.todolearn.utils.TestCoroutineRule
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
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertTaskAndGetById() = testCoroutineRule.runBlockingTest {
        // GIVEN - insert a task
        val task = Task(title = "title", description = "description")
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
    fun updateTaskAndGetById() = testCoroutineRule.runBlockingTest {
        // GIVEN - insert a task
        val originalTask = Task(
            title = "title",
            description = "description"
        )
        database.tasksDao().insert(originalTask)

        // WHEN - the task is updated
        val updatedTask = Task(
            id = originalTask.id,
            title = "new title",
            description = "new description",
            isCompleted = true
        )
        database.tasksDao().update(updatedTask)

        // THEN - the loaded data contains the expected values
        val loaded = database.tasksDao().getTaskById(originalTask.id)
        assertThat(loaded?.id, `is`(originalTask.id))
        assertThat(loaded?.title, `is`("new title"))
        assertThat(loaded?.description, `is`("new description"))
        assertThat(loaded?.isCompleted, `is`(true))
    }

    @Test
    fun insertTaskAndComplete() = testCoroutineRule.runBlockingTest {
        // GIVEN - insert a task
        val task = Task(title = "title", description = "description")
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
    fun insertTasksAndLoadAll() = testCoroutineRule.runBlockingTest {
        // GIVEN - insert 50 tasks
        val tasks = PojoTestUtils.createTasks(50)

        database.tasksDao().insertAll(tasks)

        // WHEN - get the tasks from the database
        val loaded = database.tasksDao().getTasks()

        // THEN - the loaded data contains the expected values
        assertThat(loaded as List<Task>, notNullValue())
        assertThat(loaded.size, `is` (50))
    }

    @Test
    fun insertTasksAndDeleteOne() = testCoroutineRule.runBlockingTest {
        // GIVEN - insert 2 tasks
        val tasks = PojoTestUtils.createTasks(2)
        database.tasksDao().insertAll(tasks)

        // WHEN - one task is deleted
        database.tasksDao().delete(tasks[0])

        // THEN - the loaded data contains the expected values
        val loaded = database.tasksDao().getTasks()
        assertThat(loaded as List<Task>, notNullValue())
        assertThat(loaded.size, `is` (1))
        assertThat(loaded.contains(tasks[1]), `is` (true))
    }

    @Test
    fun insertTasksAndDeleteAll() = testCoroutineRule.runBlockingTest {
        // GIVEN - insert 10 tasks
        val tasks = PojoTestUtils.createTasks(10)
        database.tasksDao().insertAll(tasks)

        // WHEN - all tasks are deleted
        database.tasksDao().deleteAllTasks()

        // THEN - the loaded data contains the expected values
        val loaded = database.tasksDao().getTasks()
        assertThat(loaded as List<Task>, notNullValue())
        assertThat(loaded.size, `is` (0))
    }

    @Test
    fun insertTasksAndDeleteCompleted() = testCoroutineRule.runBlockingTest {
        // GIVEN - insert 15 active tasks and 10 completed tasks
        val active = PojoTestUtils.createActiveTasks(15)
        val completed = PojoTestUtils.createCompletedTasks(10)
        database.tasksDao().insertAll(active)
        database.tasksDao().insertAll(completed)

        // WHEN - completed tasks are deleted
        database.tasksDao().deleteCompletedTasks()

        // THEN - the loaded data contains the expected values
        val loaded = database.tasksDao().getTasks()
        assertThat(loaded as List<Task>, notNullValue())
        assertThat(loaded.size, `is` (15))
        assertThat(loaded.containsAll(active), `is` (true))
    }
}