package com.example.habittracker.domain.models

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Time @Inject constructor() {

    fun getCurrentUtcDateInInt(): Int {
        val currentMoment = Calendar.getInstance()
        val timeInMillis = currentMoment.timeInMillis
        val dateWithoutOffset = timeInMillis / MILLIS_IN_SECOND
        return dateWithoutOffset.toInt()
    }

    fun mapUtcDateInIntToString(dateInSecond: Int): String {
        val timeInMillis = dateInSecond.toLong() * MILLIS_IN_SECOND
        val locale = Locale.getDefault()
        val sdf = SimpleDateFormat("dd-MM-yyyy", locale)
        return sdf.format(timeInMillis)
    }

    companion object {
        const val MILLIS_IN_SECOND = 1000

    }
}