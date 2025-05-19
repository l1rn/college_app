package com.example.collage.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.collage.database.Converters
import java.time.LocalDate

@Entity(tableName = "schedules")
data class Schedule(
    @PrimaryKey val scheduleId: Int,
    val groupId: Int? = null,
    val dateWeek: LocalDate? = null,
    @TypeConverters(Converters::class)
    val items: List<SubjectTime>? = null
)
