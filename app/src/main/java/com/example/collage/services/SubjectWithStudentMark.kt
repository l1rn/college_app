package com.example.collage.services

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.collage.models.Subject
import com.example.collage.models.User


@Entity(
//    tableName = "subject_marks",
    primaryKeys = ["userId", "subjectId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Subject::class,
            parentColumns = ["subjectId"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index("subjectId")]
)
data class SubjectWithStudentMark (
    val userId: Int,
    val subjectId: Int,
    val marks: List<Int>
)