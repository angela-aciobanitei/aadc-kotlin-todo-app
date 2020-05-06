package com.ang.acb.todolearn.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Immutable model class for a task item.
 */
const val NO_DEADLINE = -1L

@Entity(tableName = "tasks")
data class Task(
    val title: String = "",
    val description: String = "",
    @ColumnInfo(name = "completed")
    val isCompleted: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    val deadline: Long = NO_DEADLINE,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)
