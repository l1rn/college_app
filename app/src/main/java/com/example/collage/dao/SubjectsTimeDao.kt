package com.example.collage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.collage.models.SubjectTime

@Dao
interface SubjectsTimeDao {
    @Insert
    suspend fun insertAll(items: List<SubjectTime>)

    @Query("SELECT COUNT(*) FROM subjectsTime")
    suspend fun count(): Int

    @Query("SELECT * FROM subjectsTime")
    suspend fun getAllSubjectsTime(): List<SubjectTime>

//    @Query("SELECT teacherId FROM subjectsTime WHERE subjectId = :subjectId")
//    suspend fun getTeacherIdById(subjectId: Int): Int

    @Query("SELECT * FROM subjectsTime WHERE subjectId = :subjectId")
    suspend fun getSubjectById(subjectId: Int): SubjectTime
}