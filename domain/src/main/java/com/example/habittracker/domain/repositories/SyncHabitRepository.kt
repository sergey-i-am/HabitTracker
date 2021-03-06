package com.example.habittracker.domain.repositories

import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.models.Habit

interface SyncHabitRepository {

    suspend fun uploadAllToCloud(habitList: List<Habit>): Either<IoError, Unit>

    suspend fun upsertAndSyncWithCloud(habit: Habit): Either<IoError, Int>

    suspend fun putAndSyncWithDb(habit: Habit): Either<IoError, String>

    suspend fun syncAllFromCloud(): Either<IoError, Unit>
}