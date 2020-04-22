package com.ang.acb.todolearn.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    @ColumnInfo(name = "completed")
    val isCompleted: Boolean = false
)

// See: https://developer.android.com/reference/kotlin/java/util/UUID