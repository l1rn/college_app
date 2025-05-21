package com.example.collage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.example.collage.models.Subject

@Dao
interface SubjectDao {
    @Insert
    suspend fun insertAll(subjects: List<Subject>)

    @Query("SELECT COUNT(*) FROM subjects")
    suspend fun count(): Int

    @Query("SELECT * FROM subjects WHERE subjectId = :subjectId")
    suspend fun getSubjectById(subjectId: Int): Subject

}