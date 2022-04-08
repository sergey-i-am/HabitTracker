package com.example.habittracker.presentation.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.databinding.ItemHabitBinding
import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.entities.HabitTime
import com.example.habittracker.presentation.entities.HabitPriorityApp
import com.example.habittracker.presentation.entities.HabitTypeApp
import java.lang.RuntimeException


class HabitItemViewHolder(
    private val binding: ItemHabitBinding,
    private val onHabitListClickListener: ((HabitItem) -> Unit)?,
    private val onButtonHabitDoneClickListener: ((HabitItem) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    private val habitTime = HabitTime()

    fun bindItem(habitItem: HabitItem) {
        with(binding) {
            tvDescription.text = habitItem.doneDates.toString()
            tvName.text = habitItem.name
            tvDate.text = habitTime.mapUtcDateInIntToString(habitItem.date)
            val habitTypeApp = HabitTypeApp.fromNonNullableHabitType(habitItem.type)
            tvType.text = root.resources.getString(habitTypeApp.resourceId)
            val habitPriorityApp = HabitPriorityApp.fromHabitPriority(habitItem.priority)
            tvPriority.text = root.resources.getString(habitPriorityApp.resourceId)
            btnHabitDone.setBackgroundColor(habitItem.color)
            btnHabitDone.setOnClickListener {
                onButtonHabitDoneClickListener?.invoke(habitItem)
            }
            val recurrenceNumber = habitItem.recurrenceNumber
            val recurrenceTimes = root.resources.getQuantityString(
                R.plurals.plurals_times,
                recurrenceNumber,
                recurrenceNumber
            )
            val recurrencePeriod = habitItem.recurrencePeriod
            val recurrenceDays = root.resources.getQuantityString(
                R.plurals.plurals_days,
                recurrencePeriod,
                recurrencePeriod
            )
            tvRecurrence.text = root.resources.getString(
                R.string.recurrence,
                recurrenceTimes,
                recurrenceDays
            )
            root.setOnClickListener {
                onHabitListClickListener?.invoke(habitItem)
            }
        }
    }
}