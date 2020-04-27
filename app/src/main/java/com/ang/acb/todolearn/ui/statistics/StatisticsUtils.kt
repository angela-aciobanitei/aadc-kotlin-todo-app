package com.ang.acb.todolearn.ui.statistics

import com.ang.acb.todolearn.data.local.Task


internal fun getTasksStats(tasks: List<Task>?): StatsResult {

    return if (tasks == null || tasks.isEmpty()) {
        StatsResult(0f, 0f)
    } else {
        val totalTasks = tasks.size
        val completedTasks = tasks.count { it.isCompleted }
        StatsResult(
            activeTasksPercent = 100f * (totalTasks - completedTasks) / tasks.size,
            completedTasksPercent = 100f * completedTasks / tasks.size
        )
    }
}


data class StatsResult(
    val activeTasksPercent: Float,
    val completedTasksPercent: Float
)