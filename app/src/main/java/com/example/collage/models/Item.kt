package com.example.collage.models

import androidx.room.PrimaryKey

data class Item(
    @PrimaryKey
    val itemId: Int,
    val name: String
)