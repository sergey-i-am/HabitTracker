package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.repositories.HabitRepository
import com.example.habittracker.domain.models.HabitDone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddHabitDoneToDbUseCase @Inject constructor(private val habitRepository: HabitRepository) { //TODO add dispatcher

    suspend operator fun invoke(habitDone: HabitDone): Int {
        return habitRepository.addHabitDone(habitDone)
    }
}