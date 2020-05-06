package com.ang.acb.todolearn.ui.statistics


import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.utils.PojoTestUtils
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

/**
 * Unit tests for [getTasksStats].
 */
class StatisticsUtilsTest {

    @Test
    fun getTasksStats_noCompletedTasks_returnsHundredAndZero() {
        // Given a task list with 10 active tasks
        val tasks = PojoTestUtils.createActiveTasks(10)

        // When the task stats is computed with no completed tasks
        val result = getTasksStats(tasks)

        // Then the percentages are 100 for active tasks and 0 for completed tasks
        assertThat(result.activeTasksPercent, `is`(100f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }

    @Test
    fun getTasksStats_noActive_returnsZeroAndHundred() {
        // Given a task list with 10 completed tasks
        val tasks = PojoTestUtils.createCompletedTasks(10)

        // When the task stats is computed with no active tasks
        val result = getTasksStats(tasks)

        // Then the percentages are 0 and 100
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(100f))
    }

    @Test
    fun getTasksStats_activeAndCompleted_returnsFortySixty() {
        // Given 3 completed tasks and 2 active tasks
        val tasks = listOf(
            Task(title = "title1", description = "desc1", isCompleted = true),
            Task(title = "title2", description = "desc2", isCompleted = true),
            Task(title = "title3", description = "desc3", isCompleted = true),
            Task(title = "title4", description = "desc4", isCompleted = false),
            Task(title = "title5", description = "desc5", isCompleted = false)
        )

        // When the list of tasks is computed
        val result = getTasksStats(tasks)

        // Then the result is 40-60
        assertThat(result.activeTasksPercent, `is`(40f))
        assertThat(result.completedTasksPercent, `is`(60f))
    }

    @Test
    fun getTasksStats_error_returnsZeros() {
        // When there's an error loading stats
        val result = getTasksStats(null)

        // Both active and completed tasks percentages are 0
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }

    @Test
    fun getTasksStats_empty_returnsZeros() {
        // When there are no tasks
        val result = getTasksStats(emptyList())

        // Both active and completed tasks percentages are 0
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }
}