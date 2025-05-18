package com.example.collage.models

import androidx.room.PrimaryKey
import java.time.LocalDateTime

data class ItemTime(
    @PrimaryKey val itemTimeId: Int,
    val itemId: Int,
    val lessonStart: LocalDateTime,
    val lessonEnd: LocalDateTime
)
