package com.example.habittracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.habittracker.domain.HabitPriority
import com.example.habittracker.domain.HabitType

@Entity(
    tableName = "habit_items",
    indices = [Index("name", unique = true)]
)
data class HabitItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    var description: String,
    var priority: HabitPriority,
    var type: HabitType,
    var color: Int,
    @ColumnInfo(name = "recurrence_number")
    var recurrenceNumber: Int,
    @ColumnInfo(name = "recurrence_period")
    var recurrencePeriod: Int
)
