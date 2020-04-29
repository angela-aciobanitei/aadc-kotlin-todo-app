package com.ang.acb.todolearn.ui.common

import android.view.Gravity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.ServiceLocator
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.data.repo.ITasksRepository
import com.ang.acb.todolearn.util.EspressoIdlingResource.countingIdlingResource
import com.ang.acb.todolearn.utils.DataBindingIdlingResource
import com.ang.acb.todolearn.utils.getToolbarNavigationContentDescription
import com.ang.acb.todolearn.utils.monitorActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {

    private lateinit var tasksRepository: ITasksRepository

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun initRepository() {
        tasksRepository = ServiceLocator.provideTasksRepository(getApplicationContext())
    }

    @After
    fun resetRepository() = ServiceLocator.resetRepository()

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun tasksScreen_clickOnHomeIcon_opensDrawer() {
        // Launch main screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Check that left drawer is closed at startup
        onView(withId(R.id.root_drawer_layout))
            .check(matches(isClosed(Gravity.START)))

        // Open drawer by clicking the drawer icon
        onView(withContentDescription(activityScenario.getToolbarNavigationContentDescription()))
            .perform(click())

        // Check if drawer is open
        onView(withId(R.id.root_drawer_layout))
            .check(matches(isOpen(Gravity.START)))

        // When using ActivityScenario.launch(), always call close()
        activityScenario.close()
    }

    @Test
    fun taskDetailScreenToEditScreen_doubleUpButton_returnsToHomeScreen() = runBlocking {
        val task = Task("Title", "Description")
        tasksRepository.saveTask(task)

        // Launch Tasks screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the task item in the list
        onView(withText("Title")).perform(click())

        // Click on the edit task FAB button
        onView(withId(R.id.edit_task_fab)).perform(click())

        // Check that if we click the Up button once,
        // then we end up back at the task details page.
        onView(withContentDescription(activityScenario.getToolbarNavigationContentDescription()))
            .perform(click())
          onView(withId(R.id.task_details_title_tv)).check(matches(isDisplayed()))

        // Confirm that if we click the Up button a second time,
        // we end up back at the home screen.
        onView(withContentDescription(activityScenario.getToolbarNavigationContentDescription()))
            .perform(click())
        onView(withId(R.id.tasks_coordinator_layout)).check(matches(isDisplayed()))

        // When using ActivityScenario.launch, always call close()
        activityScenario.close()
    }
}