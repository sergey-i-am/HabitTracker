package com.example.habittracker.domain.repositories

import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitWithDone
import com.example.habittracker.domain.models.UpsertException

interface SyncHabitRepository {

    suspend fun uploadAllToCloud(habitList: List<HabitWithDone>)

    suspend fun upsertAndSyncWithCloud(habit: Habit): Either<UpsertException, Int>

    suspend fun putAndSyncWithDb(habit: Habit, newHabitId: Int): String?

    suspend fun syncAllFromCloud()

    suspend fun syncAllToCloud()
}