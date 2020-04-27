package com.ang.acb.todolearn.statistics


import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.ui.statistics.getTasksStats
import com.ang.acb.todolearn.util.TestUtil
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
        val tasks = TestUtil.createActiveTasks(10)

        // When the task stats is computed with no completed tasks
        val result = getTasksStats(tasks)

        // Then the percentages are 100 for active tasks and 0 for completed tasks
        assertThat(result.activeTasksPercent, `is`(100f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }

    @Test
    fun getTasksStats_noActive_returnsZeroAndHundred() {
        // Given a task list with 10 completed tasks
        val tasks = TestUtil.createCompletedTasks(10)

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
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = false),
            Task("title", "desc", isCompleted = false)
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