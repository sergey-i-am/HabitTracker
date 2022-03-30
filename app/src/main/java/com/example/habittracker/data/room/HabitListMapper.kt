package com.example.habittracker.data.room

import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.entities.HabitPriority

class HabitListMapper {

    fun mapEntityToDbModel(habitItem: HabitItem) = HabitItemDbModel(
        id = habitItem.id,
        name = habitItem.name,
        description = habitItem.description,
        priority = habitItem.priority.id,
        type = habitItem.type,
        color = habitItem.color,
        recurrenceNumber = habitItem.recurrenceNumber,
        recurrencePeriod = habitItem.recurrencePeriod,
        date = habitItem.date
    )

    fun mapDbModelToEntity(habitItemDbModel: HabitItemDbModel) = HabitItem(
        name = habitItemDbModel.name,
        description = habitItemDbModel.description,
        priority = HabitPriority.getPriorityById(habitItemDbModel.priority),
        type = habitItemDbModel.type,
        color = habitItemDbModel.color,
        recurrenceNumber = habitItemDbModel.recurrenceNumber,
        recurrencePeriod = habitItemDbModel.recurrencePeriod,
        id = habitItemDbModel.id,
        date = habitItemDbModel.date
    )

    fun mapListDbModelToEntity(list: List<HabitItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}