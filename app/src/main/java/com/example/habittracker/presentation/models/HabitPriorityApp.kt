package com.example.habittracker.presentation.models

import androidx.annotation.StringRes
import com.example.habittracker.R
import com.example.habittracker.domain.models.HabitPriority

enum class HabitPriorityApp(@StringRes val resourceId: Int, val id: Int) {
    LOW(R.string.low_priority, 0),
    NORMAL(R.string.normal_priority, 1),
    HIGH(R.string.high_priority, 2);

    fun toHabitPriority() = HabitPriority.valueOf(this.name)

    companion object {
        fun fromHabitPriority(habitPriority: HabitPriority) = valueOf(habitPriority.name)
    }
}
