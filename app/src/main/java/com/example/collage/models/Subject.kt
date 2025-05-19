package com.example.collage.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Subject(
    @PrimaryKey
    val subjectId: Int,
    val name: String
)