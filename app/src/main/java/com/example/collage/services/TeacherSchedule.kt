package com.example.collage.services

import androidx.room.Embedded
import androidx.room.Relation
import com.example.collage.models.Schedule
import com.example.collage.models.User

data class TeacherSchedule(
    @Embedded val teacher: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "teacherId"
    )
    val items: List<Schedule>
)