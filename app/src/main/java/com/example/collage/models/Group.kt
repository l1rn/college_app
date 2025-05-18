package com.example.collage.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class Group(
    @PrimaryKey val groupId: Int,
    val name: String
)