package com.example.habittracker.domain.usecases.common

import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.repositories.SyncHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PutHabitAndSyncWithDbUseCase @Inject constructor(
    private val syncHabitRepository: SyncHabitRepository,
) {

    suspend operator fun invoke(habit: Habit, newHabitId: Int): Either<IoError, String> =
        syncHabitRepository.putAndSyncWithDb(habit, newHabitId)

}
