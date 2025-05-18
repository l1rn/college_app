package com.example.collage.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.collage.models.Group

@Dao
interface GroupDao {
    @Insert
    suspend fun insertAll(groups: List<Group>)
}