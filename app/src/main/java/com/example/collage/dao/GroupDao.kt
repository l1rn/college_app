package com.example.collage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.collage.models.Group

@Dao
interface GroupDao {
    @Insert
    suspend fun insertAll(groups: List<Group>)

    @Query("SELECT * FROM `groups` WHERE groupId = :groupId")
    suspend fun getGroupFromId(groupId: Int?): Group
}