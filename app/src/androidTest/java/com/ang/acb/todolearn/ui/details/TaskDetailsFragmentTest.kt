package com.ang.acb.todolearn.ui.details

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ang.acb.todolearn.fakes.FakeTasksRepository
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.ServiceLocator
import com.ang.acb.todolearn.TestCoroutineRule
import com.ang.acb.todolearn.data.local.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TaskDetailsFragmentTest {

    private lateinit var fakeTasksRepository: FakeTasksRepository

    // Sets the main coroutines dispatcher to a TestCoroutineDispatcher
    // with a TestCoroutineScope.
    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
        // Swap the real tasks repository with the fake version
        // using the Service Locator design pattern
        fakeTasksRepository = FakeTasksRepository()
        ServiceLocator.tasksRepository = fakeTasksRepository
    }

    @After
    fun tearDown() {
        mainCoroutineRule.runBlockingTest {
            ServiceLocator.resetRepository()
        }
    }

    @Test
    fun clickEditFab_navigatesToAddEditTaskFragment()  = mainCoroutineRule.runBlockingTest{
        // Given - save active task via repository
        val task = Task("Title", "Description", false)
        fakeTasksRepository.saveTask(task)

        // launch the task details screen
        val scenario = launchFragmentInContainer<TaskDetailsFragment>(
            TaskDetailsFragmentArgs(task.id).toBundle(),
            R.style.Base_AppTheme
        )

        // Mock NavController and associate it with this fragment's view
        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // When - user clicks the Edit Task FAB
        onView(withId(R.id.edit_task_fab)).perform(click())

        // Then - the navigation to AddEditTaskFragment is triggered
        verify(navController).navigate(
            TaskDetailsFragmentDirections.actionTaskDetailsFragmentToAddEditTaskFragment(
                taskId = task.id,
                title = getApplicationContext<Context>().getString(R.string.edit_task)
            )
        )
    }

    @Test
    fun activeTaskDetails_areDisplayedInUi() = mainCoroutineRule.runBlockingTest{
        // GIVEN - Add active (incomplete) task to the DB
        val activeTask = Task("Active Task", "Active Task Description", false)
        fakeTasksRepository.saveTask(activeTask)

        // WHEN - Details fragment launched to display task
        val bundle = TaskDetailsFragmentArgs(activeTask.id).toBundle()
        launchFragmentInContainer<TaskDetailsFragment>(bundle, R.style.Base_AppTheme)

        // THEN - Task details are displayed on the screen
        onView(withId(R.id.task_details_title_tv)).check(matches(isDisplayed()))
        onView(withId(R.id.task_details_title_tv)).check(matches(withText("Active Task")))
        onView(withId(R.id.task_details_description_tv)).check(matches(isDisplayed()))
        onView(withId(R.id.task_details_description_tv)).check(matches(withText("Active Task Description")))
        onView(withId(R.id.task_details_completed_checkbox)).check(matches(isDisplayed()))
        onView(withId(R.id.task_details_completed_checkbox)).check(matches(not(isChecked())))
    }

    @Test
    fun completedTaskDetails_areDisplayedInUi() = mainCoroutineRule.runBlockingTest{
        // GIVEN - Add completed task to the DB
        val activeTask = Task("Completed Task", "Completed Task Description", true)
        fakeTasksRepository.saveTask(activeTask)

        // WHEN - Details fragment launched to display task
        val bundle = TaskDetailsFragmentArgs(activeTask.id).toBundle()
        launchFragmentInContainer<TaskDetailsFragment>(bundle, R.style.Base_AppTheme)

        // THEN - Task details are displayed on the screen
        onView(withId(R.id.task_details_title_tv)).check(matches(isDisplayed()))
        onView(withId(R.id.task_details_title_tv)).check(matches(withText("Completed Task")))
        onView(withId(R.id.task_details_description_tv)).check(matches(isDisplayed()))
        onView(withId(R.id.task_details_description_tv)).check(matches(withText("Completed Task Description")))
        onView(withId(R.id.task_details_completed_checkbox)).check(matches(isDisplayed()))
        onView(withId(R.id.task_details_completed_checkbox)).check(matches(isChecked()))
    }
}