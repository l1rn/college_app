package com.example.collage.database

import androidx.room.TypeConverter
import com.example.collage.models.SubjectTime
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): String = dateTime.toString()

    @TypeConverter
    fun toLocalDateTime(value: String): LocalDateTime = LocalDateTime.parse(value)

    @TypeConverter
    fun fromSubjectTimeList(list: List<SubjectTime>): String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toSubjectTimeList(value: String): List<SubjectTime>{
        val type = object : TypeToken<List<SubjectTime>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromListToString(marks: List<Int>): String {
        return Gson().toJson(marks)
    }

    @TypeConverter
    fun fromStringToList(marksString: String): List<Int>{
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(marksString, type)
    }
}