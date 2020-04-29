package com.ang.acb.todolearn.ui.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ang.acb.todolearn.FakeTasksRepository
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.ServiceLocator
import com.ang.acb.todolearn.TestCoroutineRule
import com.ang.acb.todolearn.data.local.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify

/**
 * Integration test for the [TasksFragment].
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TasksFragmentTest {

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
    fun clickAddNewTaskFab_navigatesToAddEditTaskFragment() {
        // Given - Launch TasksFragment
        val scenario = launchFragmentInContainer<TasksFragment>(
            Bundle(), R.style.Base_AppTheme
        )

        // Mock NavController and associate it with this fragment's view.
        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // When - user clicks the Add New Task FAB
        onView(withId(R.id.add_new_task_fab)).perform(click())

        // Then - verify that the navigation to AddEditTaskFragment is triggered
        // Note: to avoid NotAMockException, make sure you call verify() correctly!
        //      verify(mock).someMethod();
        //      verify(mock, times(10)).someMethod();
        //      verify(mock, atLeastOnce()).someMethod();
        verify(navController).navigate(
            TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                taskId = null,
                title = ApplicationProvider.getApplicationContext<Context>().getString(R.string.new_task)
            )
        )
    }

    /**
     * To fix this : "IllegalStateException: Can not perform this action after onSaveInstanceState",
     * make sure the device you are running the tests on is unlocked.
     *
     * To fix this: "androidx.test.espresso.PerformException: Animations or transitions are
     * enabled on the target device." For Espresso UI testing, you should turn animations off.
     * On your testing device, go to Settings > Developer options and disable these 3 settings:
     * Window animation scale, Transition animation scale, and Animator duration scale.
     * https://developer.android.com/training/testing/espresso/setup#set-up-environment
     */
    @Test
    fun clickTaskItem_navigatesToTaskDetailsFragment() = mainCoroutineRule.runBlockingTest {
        // Given - 3 tasks in the repository
        fakeTasksRepository.saveTasks(listOf(
            Task("Title1", "Description1", false, "id"),
            Task("Title2", "Description2", true, "id2"),
            Task("Title3", "Description3", true, "id3")))

        // Launch the TaskFragment and mock the nav controller
        val scenario=
            launchFragmentInContainer<TasksFragment>(Bundle(), R.style.Base_AppTheme)
        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // When - clicking on the first list item
        onView(withId(R.id.rv_tasks))
            .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText("Title1")), click())
            )

        // Then - verify that we navigate to the first detail screen
        verify(navController).navigate(
            TasksFragmentDirections.actionTasksFragmentToTaskDetailsFragment( "id1")
        )
    }
}