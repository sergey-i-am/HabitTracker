package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.models.UpsertException
import com.example.habittracker.domain.repositories.DbHabitRepository
import com.example.habittracker.domain.models.HabitItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpsertHabitToDbUseCase @Inject constructor(val dbHabitRepository: DbHabitRepository) {

    suspend operator fun invoke(habitItem: HabitItem): UpsertException? {
        return dbHabitRepository.upsertHabit(habitItem)
    }
}