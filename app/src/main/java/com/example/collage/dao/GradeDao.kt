package com.example.collage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.collage.models.Grade

@Dao
interface GradeDao {
    @Insert
    suspend fun insertAll(grades: List<Grade>)

    @Query("SELECT COUNT(*) FROM grades")
    suspend fun count(): Int

    @Query("SELECT * FROM grades WHERE studentId = :studentId AND subjectId = :subjectId")
    suspend fun getGrades(studentId: Int, subjectId: Int): List<Grade>

    @Insert
    suspend fun insert(grade: Grade)
}