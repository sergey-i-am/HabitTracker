package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.HabitRepository

class DeleteHabitItemUseCase(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitItem: HabitItem) {
        habitRepository.delete(habitItem)
    }
}