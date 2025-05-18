package com.example.collage.models

import androidx.room.PrimaryKey
import java.time.LocalDate

data class Schedule(
    @PrimaryKey val scheduleId: Int,
    val teacherId: Int,
    val dateWeek: LocalDate,
    val items: List<ItemTime>? = null
)
