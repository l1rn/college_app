package com.example.collage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.collage.models.Schedule

@Dao
interface ScheduleDao {
    @Query("SELECT COUNT(*) FROM schedules")
    suspend fun count(): Int

    @Insert
    suspend fun insertAll(schedules: List<Schedule>)

    @Query("SELECT * FROM schedules")
    suspend fun getAllSchedules(): List<Schedule>
}