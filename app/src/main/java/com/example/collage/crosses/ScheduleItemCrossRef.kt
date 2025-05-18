package com.example.collage.crosses

import androidx.room.Entity

@Entity(primaryKeys = ["scheduleId", "itemTimeId"])
data class ScheduleItemCrossRef(
    val scheduleId: Int,
    val itemTimeId: Int
)
