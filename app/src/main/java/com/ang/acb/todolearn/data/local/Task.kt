package com.ang.acb.todolearn.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Immutable model class for a task item.
 */
@Entity(tableName = "tasks")
data class Task(
    val title: String = "",
    val description: String = "",
    @ColumnInfo(name = "completed")
    val isCompleted: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    val deadline: Long = 0L,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)
