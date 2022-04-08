package com.example.habittracker.data.retrofit.api_models

import com.google.gson.annotations.SerializedName

data class HabitDoneApiModel(
    val date: Int,

    @SerializedName("habit_uid")
    val habitUid: String
)