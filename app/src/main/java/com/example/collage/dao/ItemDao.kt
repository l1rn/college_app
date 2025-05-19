package com.example.collage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.collage.models.Subject

@Dao
interface ItemDao {
    @Insert
    suspend fun insertAll(subjects: List<Subject>)

    @Query("SELECT COUNT(*) FROM items")
    suspend fun count(): Int
}