package com.ang.acb.todolearn.utils

import com.ang.acb.todolearn.data.local.Task
import kotlin.random.Random

object PojoTestUtils {

    fun createTasks(count : Int) :  List<Task> = (0 until count).map {
        Task(
            title = "title$it",
            description = "description$it",
            isCompleted = Random.nextBoolean(),
            id = "id$it"
        )
    }

    fun createActiveTasks(count : Int) :  List<Task> = (0 until count).map {
        Task(
            title = "title$it",
            description = "description$it",
            isCompleted = false,
            id = "id-aa-$it"
        )
    }

    fun createCompletedTasks(count : Int) :  List<Task> = (0 until count).map {
        Task(
            title = "title$it",
            description = "description$it",
            isCompleted = true,
            id = "id-cc-$it"
        )
    }
}