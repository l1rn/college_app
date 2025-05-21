package com.example.collage.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grades")
data class Grade(
    @PrimaryKey(autoGenerate = true) val gradeId: Int = 0,
    val studentId: Int,
    val subjectId: Int,
    val value: Int,
    val date: String
)
