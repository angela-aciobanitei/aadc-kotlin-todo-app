package com.ang.acb.todolearn.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * The [Room] database for this app.
 */
@Database(entities = [Task::class], version = 5, exportSchema = false)
abstract class TasksDatabase: RoomDatabase() {

    abstract fun tasksDao(): TasksDao
}