package com.example.collage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

object SessionManager {
    private const val KEY_STUDENT_ID = "student_id"
    private const val KEY_TEACHER_ID = "teacher_id"
    private const val KEY_SUBJECT_ID = "subject_id"

    fun saveStudentId(context: Context, id: Int){
        context.getSharedPreferences("AppPrefs", MODE_PRIVATE).edit(){
            putInt(KEY_STUDENT_ID, id)
            remove(KEY_TEACHER_ID)
            remove(KEY_SUBJECT_ID)
            apply()
        }
    }

    fun saveSubjectId(context: Context, id: Int){
        context.getSharedPreferences("AppPrefs", MODE_PRIVATE).edit(){
            putInt(KEY_SUBJECT_ID, id)
            apply()
        }
    }

    fun saveTeacherId(context: Context, id: Int){
        context.getSharedPreferences("AppPrefs", MODE_PRIVATE).edit(){
            putInt(KEY_TEACHER_ID, id)
            remove(KEY_SUBJECT_ID)
            remove(KEY_STUDENT_ID)
            apply()
        }
    }


    fun clearSession(context: Context) {
        context.getSharedPreferences("AppPrefs", MODE_PRIVATE).edit {
            remove(KEY_STUDENT_ID)
            remove(KEY_TEACHER_ID)
            remove(KEY_SUBJECT_ID)
            apply()
        }
    }
    fun removeSubjectId(context: Context){
        context.getSharedPreferences("AppPrefs", MODE_PRIVATE).edit {
            remove(KEY_SUBJECT_ID)
            apply()
        }
    }

    fun getSubjectId(context: Context): Int{
        return context.getSharedPreferences("AppPrefs", MODE_PRIVATE).getInt(KEY_SUBJECT_ID, -1)
    }

    fun getStudentId(context: Context): Int{
        return context.getSharedPreferences("AppPrefs", MODE_PRIVATE).getInt(KEY_STUDENT_ID, -1)
    }

    fun getTeacherId(context: Context):Int {
        return context.getSharedPreferences("AppPrefs", MODE_PRIVATE).getInt(KEY_TEACHER_ID, -1)
    }


}