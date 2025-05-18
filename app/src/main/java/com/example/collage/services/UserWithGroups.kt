package com.example.collage.services

import androidx.room.Embedded
import androidx.room.Relation
import com.example.collage.models.Group
import com.example.collage.models.User

data class UserWithGroups(
    @Embedded val user: User,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "groupId",
    )
    val group: Group?
)
