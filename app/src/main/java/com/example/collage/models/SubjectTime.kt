package com.example.collage.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "subjectsTime")
data class SubjectTime(
    @PrimaryKey val subjectTimeId: Int,
    val subjectId: Int,
    val teacherId: Int? = null,
    val lessonStart: LocalDateTime,
    val lessonEnd: LocalDateTime
)
