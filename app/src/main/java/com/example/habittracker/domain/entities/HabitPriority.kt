package com.example.habittracker.domain.entities

import com.example.habittracker.R

enum class HabitPriority(val resourceId: Int, val id: Int) {
    LOW(R.string.low_priority, 0),
    NORMAL(R.string.normal_priority, 1),
    HIGH(R.string.high_priority, 2);

    companion object {
        fun getPriorityByPosition(position: Int) = values()[position]
    }
}
