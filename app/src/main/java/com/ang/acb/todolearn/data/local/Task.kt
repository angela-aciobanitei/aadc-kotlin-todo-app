package com.ang.acb.todolearn.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tasks")
data class Task(
    val title: String = "",
    val description: String = "",
    @ColumnInfo(name = "completed")
    val isCompleted: Boolean = false,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)
