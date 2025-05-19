package com.example.collage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.collage.models.SubjectTime

@Dao
interface ItemsTimeDao {
    @Insert
    suspend fun insertAll(items: List<SubjectTime>)

    @Query("SELECT COUNT(*) FROM itemsTime")
    suspend fun count(): Int
}