package com.ang.acb.todolearn.ui.list

import com.ang.acb.todolearn.R

/**
 * Used to filter the tasks loaded on [TasksFragment] screen.
 */
enum class TasksFilter(val value: Int) {
    ALL_TASKS(R.string.all_tasks),
    ACTIVE_TASKS(R.string.active_tasks),
    COMPLETED_TASKS(R.string.completed_tasks)
}