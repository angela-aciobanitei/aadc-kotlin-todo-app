package com.ang.acb.todolearn.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 5, exportSchema = false)
abstract class TasksDatabase: RoomDatabase() {

    // Create an abstract val to get a reference to the DAO.
    abstract val tasksDao: TasksDao

    // Create a companion object to get a reference to the database.
    companion object {

        @Volatile
        private var INSTANCE: TasksDatabase? = null

        fun getInstance(context: Context): TasksDatabase {
            // For thread safety
            synchronized(this) {
                // For safe casting
                var tasksDatabase = INSTANCE
                if (tasksDatabase == null) {
                    tasksDatabase = Room.databaseBuilder(
                        context.applicationContext,
                        TasksDatabase::class.java,
                        "tasks.db")
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = tasksDatabase
                }

                return tasksDatabase
            }
        }
    }
}