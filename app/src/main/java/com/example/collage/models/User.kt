package com.example.collage.models

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userId: Int,
    val name: String,
    val role: Role,
    val phone: String,
    val scheduleId: Int? = null,
    val groupId: Int? = null
)
