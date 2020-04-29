package com.ang.acb.todolearn.ui.common

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.ServiceLocator
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.repo.ITasksRepository
import com.ang.acb.todolearn.util.EspressoIdlingResource
import com.ang.acb.todolearn.utils.CustomMatchers.Companion.withItemCount
import com.ang.acb.todolearn.utils.DataBindingIdlingResource
import com.ang.acb.todolearn.utils.monitorActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Large End-to-End test for the tasks module.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var repository: ITasksRepository

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
        repository = ServiceLocator.provideTasksRepository(getApplicationContext())
        runBlocking {
            repository.deleteAllTasks()
        }
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun createOneTask_deleteTask() {
        // Launch Tasks screen
        val activityScenario
                = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Add one active task
        onView(withId(R.id.add_new_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text))
            .perform(typeText("TITLE1"), closeSoftKeyboard())
        onView(withId(R.id.add_task_description_edit_text))
            .perform(typeText("DESCRIPTION1"), closeSoftKeyboard())
        onView(withId(R.id.save_task_fab)).perform(click())

        // Click on task item to open task details fragment
        onView(withText("TITLE1")).perform(click())

        // Click delete task menu item
        onView(withId(R.id.delete_task)).perform(click())

        // Click on OK button of the alert dialog
        // https://stackoverflow.com/questions/39376856/how-to-use-espresso-to-press-a-alertdialog-button
        onView(withText("OK"))
            .inRoot(isDialog()) // <-- this is important
            .check(matches(isDisplayed()))
            .perform(click());

        // Verify the task was deleted
        openActionBarOverflowOrOptionsMenu(getApplicationContext())
        onView(withText(getApplicationContext<Context>().getString(R.string.show_all_tasks)))
            .perform(click())
        onView(withText("TITLE1")).check(doesNotExist())

        // Make sure the activity is closed
        activityScenario.close()
    }

    /**
     * Note use runBlocking{} instead of runBlockingTest{}, to avoid
     * "java.lang.IllegalStateException: This job has not completed yet"
     *
     * https://github.com/Kotlin/kotlinx.coroutines/issues/1204
     */
    @Test
    fun editTask() = runBlocking {
        // Save an active task in via the repository
        repository.saveTask(Task("TITLE1", "DESCRIPTION1"))

        // Launch the Tasks screen
        val activityScenario
                = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the task item found in the list and verify that all the data is correct
        onView(withText("TITLE1")).perform(click())
        onView(withId(R.id.task_details_title_tv)).check(matches(withText("TITLE1")))
        onView(withId(R.id.task_details_description_tv)).check(matches(withText("DESCRIPTION1")))
        onView(withId(R.id.task_details_completed_checkbox)).check(matches(not(isChecked())))

        // Click on the edit button, edit, and save
        onView(withId(R.id.edit_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("NEW TITLE"))
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("NEW DESCRIPTION"))
        onView(withId(R.id.save_task_fab)).perform(click())

        // Verify task is displayed on screen in the task list.
        onView(withText("NEW TITLE")).check(matches(isDisplayed()))
        // Verify previous task is not displayed
        onView(withText("TITLE1")).check(doesNotExist())

        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }

    @Test
    fun clearCompletedTasks() = runBlocking {
        // Given 3 tasks, one active, two completed
        repository.saveTask(Task("Title1", "Description1"))
        repository.saveTask(Task("Title2", "Description2", true))
        repository.saveTask(Task("Title3", "Description3", true))

        // Launch the Tasks screen
        val activityScenario
                = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Verify that the recycler view holds 3 items
        onView(withId(R.id.rv_tasks)).check(matches(withItemCount(3)))

        // Open the overflow menu and click on "Clear completed tasks" menu item
        openActionBarOverflowOrOptionsMenu(getApplicationContext())
        onView(withText(getApplicationContext<Context>().getString(R.string.clear_completed_tasks)))
            .perform(click())

        // Verify that the completed tasks are not displayed on the screen
        onView(withText("Title1")).check(matches(isDisplayed()))
        onView(withText("Title2")).check(doesNotExist())
        onView(withText("Title3")).check(doesNotExist())

        // Make sure the activity is closed before resetting the database.
        activityScenario.close()
    }

    @Test
    fun clearAllTasks() = runBlocking {
        // Given 5 tasks
        repository.saveTask(Task("Title1", "Description1"))
        repository.saveTask(Task("Title2", "Description2", true))
        repository.saveTask(Task("Title3", "Description3", true))
        repository.saveTask(Task("Title4", "Description4"))
        repository.saveTask(Task("Title5", "Description5", true))

        // Launch the Tasks screen
        val activityScenario
                = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Verify that the recycler view holds 5 items
        onView(withId(R.id.rv_tasks)).check(matches(withItemCount(5)))

        // Open the overflow menu and click on "Clear completed tasks" menu item
        openActionBarOverflowOrOptionsMenu(getApplicationContext())
        onView(withText(getApplicationContext<Context>().getString(R.string.clear_all_tasks)))
            .perform(click())

        // Verify that the recycler view has 0 items
        onView(withId(R.id.rv_tasks)).check(matches(withItemCount(0)))
        onView(withText("Title1")).check(matches(not(isDisplayed())))
        onView(withText("Title2")).check(matches(not(isDisplayed())))
        onView(withText("Title3")).check(matches(not(isDisplayed())))
        onView(withText("Title4")).check(matches(not(isDisplayed())))
        onView(withText("Title5")).check(matches(not(isDisplayed())))

        // Make sure the activity is closed before resetting the database.
        activityScenario.close()
    }
}