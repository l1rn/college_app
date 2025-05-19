package com.example.collage.services

import androidx.room.Embedded
import androidx.room.Relation
import com.example.collage.models.User

data class SubjectWithStudentMark (
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "itemId"
    )
    val marks: List<Int>
)