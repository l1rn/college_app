package com.example.collage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.example.collage.models.Subject
import com.example.collage.services.SubjectWithStudentMark

@Dao
interface SubjectDao {
    @Insert
    suspend fun insertAll(subjects: List<Subject>)

    @Insert
    suspend fun insertAllWithMarks(marksList: List<SubjectWithStudentMark>)

    @Query("SELECT COUNT(*) FROM subjects")
    suspend fun count(): Int

    @Query("SELECT * FROM subjects WHERE subjectId = :subjectId")
    suspend fun getSubjectById(subjectId: Int): Subject

//    @RewriteQueriesToDropUnusedColumns
//    @Query("SELECT marks FROM subject_marks WHERE subjectId = :subjectId")
//    suspend fun getMarksById(subjectId: Int): String
//
//    @Query("SELECT COUNT(*) FROM subject_marks")
//    suspend fun countMarks(): Int
}