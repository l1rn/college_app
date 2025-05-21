package com.example.collage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.collage.dao.GradeDao
import com.example.collage.dao.GroupDao
import com.example.collage.dao.SubjectDao
import com.example.collage.dao.SubjectsTimeDao
import com.example.collage.dao.UserDao
import com.example.collage.models.Grade
import com.example.collage.models.Group
import com.example.collage.models.Subject
import com.example.collage.models.SubjectTime
import com.example.collage.models.Role
import com.example.collage.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

@Database(
    entities = [
        User::class,
        Group::class,
        Subject::class,
        SubjectTime::class,
        Grade::class,
               ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun userDao(): UserDao
    abstract fun subjectDao(): SubjectDao
    abstract fun gradeDao(): GradeDao
    abstract fun subjectsTimeDao(): SubjectsTimeDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "college_db"
                )
                .addCallback(DatabaseCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val database = getDatabase(context)
                initDefaultDatabase(database)
            }
        }

        private suspend fun initDefaultDatabase(db: AppDatabase){
            if(db.userDao().count() == 0){
                val groups = listOf(
                    Group(groupId = 1, name = "ИП-302"),
                    Group(groupId = 2, name = "ИП-101"),
                    Group(groupId = 3, name = "ПО-205"),
                    Group(groupId = 4, name = "КН-401"),
                    Group(groupId = 5, name = "БИ-103"),
                    Group(groupId = 6, name = "ИВТ-202")
                )
                db.groupDao().insertAll(groups)
                val usersWithGroup = listOf(
                    User(userId = 1, name = "Романов Роман Романович", role = Role.STUDENT, phone = "8777666555", groupId = 1),
                    User(userId = 14, name = "Романов Егор Романович", role = Role.STUDENT, phone = "8777666444", groupId = 1),
                    User(userId = 13, name = "Романов Георгий Романович", role = Role.STUDENT, phone = "8777666777", groupId = 1),
                    User(userId = 2, name = "Иванов Иван Иванович", role = Role.STUDENT, phone = "87775554433", groupId = 2),
                    User(userId = 3, name = "Петрова Анна Сергеевна", role = Role.STUDENT, phone = "87011234567", groupId = 3),
                    User(userId = 4, name = "Смирнов Алексей Дмитриевич", role = Role.STUDENT, phone = "87778889900",groupId = 4),
                    User(userId = 5, name = "Кузнецова Екатерина Викторовна", role = Role.STUDENT, phone = "87022345678",groupId = 5),
                    User(userId = 6, name = "Григорьев Денис Олегович", role = Role.STUDENT, phone = "87771231234", groupId = 6)
                )
                val teachers = listOf(
                    User(userId = 7, name = "Дроздов Георгий Дмитриевич", role = Role.TEACHER, phone = "8000777666"),
                    User(userId = 8, name = "Иванова Анна Сергеевна", role = Role.TEACHER, phone = "79001234567"),
                    User(userId = 9, name = "Петров Владислав Игоревич", role = Role.TEACHER, phone = "79215553344"),
                    User(userId = 10, name = "Смирнова Ольга Викторовна", role = Role.TEACHER, phone = "78122456789"),
                    User(userId = 11, name = "Кузнецов Артём Александрович", role = Role.STUDENT, phone = "79637891234"),
                    User(userId = 12, name = "Фёдорова Екатерина Павловна", role = Role.TEACHER, phone = "79998765432")
                )
                db.userDao().insertAll(usersWithGroup)
                db.userDao().insertAll(teachers)
            }

            if(db.subjectDao().count() == 0){
                val subjects = listOf(
                    Subject(subjectId = 1, name = "Математика"),
                    Subject(subjectId = 2, name = "Русский"),
                    Subject(subjectId = 3, name = "Информатика"),
                    Subject(subjectId = 4, name = "Программирование"),
                    Subject(subjectId = 5, name = "Физика"),
                    Subject(subjectId = 6, name = "Операционные системы"),
                    Subject(subjectId = 7, name = "vim обучение"),
                    Subject(subjectId = 8, name = "Школота")
                )
                db.subjectDao().insertAll(subjects)
            }


            if(db.subjectsTimeDao().count() == 0){
                val monday = listOf(
                    // Понедельник
                    SubjectTime(
                        subjectTimeId = 1,
                        subjectId = 1,
                        teacherId = 8,
                        groupId = 1,
                        lessonStart = LocalDateTime.of(2025, Month.JUNE, 23, 9, 0),  // 23 июня (пн)
                        lessonEnd = LocalDateTime.of(2025, Month.JUNE, 23, 10, 30)
                    ),
                    SubjectTime(
                        subjectTimeId = 2,
                        subjectId = 2,
                        teacherId = 9,
                        groupId = 1,
                        lessonStart = LocalDateTime.of(2025, Month.JUNE, 23, 10, 40), // Перемена 10 мин
                        lessonEnd = LocalDateTime.of(2025, Month.JUNE, 23, 12, 10)
                    ),
                )

                val tuesday = listOf(
                    // Вторник
                    SubjectTime(
                        subjectTimeId = 3,
                        subjectId = 3,
                        teacherId = 7,
                        groupId = 2,

                        lessonStart = LocalDateTime.of(2025, Month.JUNE, 24, 8, 30),  // 24 июня (вт)
                        lessonEnd = LocalDateTime.of(2025, Month.JUNE, 24, 10, 0)
                    ),
                    SubjectTime(
                        subjectTimeId = 4,
                        subjectId = 4,
                        teacherId = 7,
                        groupId = 2,
                        lessonStart = LocalDateTime.of(2025, Month.JUNE, 24, 10, 20), // Перемена 20 мин
                        lessonEnd = LocalDateTime.of(2025, Month.JUNE, 24, 11, 50)
                    ),
                )

                val wednesday = listOf(
                    // Среда
                    SubjectTime(
                        subjectTimeId = 5,
                        subjectId = 5,
                        teacherId = 10,
                        groupId = 2,
                        lessonStart = LocalDateTime.of(2025, Month.JUNE, 25, 13, 0),  // 25 июня (ср) - день начинается позже
                        lessonEnd = LocalDateTime.of(2025, Month.JUNE, 25, 14, 30)
                    ),
                    SubjectTime(
                        subjectTimeId = 6,
                        subjectId = 6,
                        teacherId = 11,
                        groupId = 3,
                        lessonStart = LocalDateTime.of(2025, Month.JUNE, 25, 14, 40), // Перемена 10 мин
                        lessonEnd = LocalDateTime.of(2025, Month.JUNE, 25, 16, 10)
                    ),
                )
                val thursday = listOf(
                    // Четверг
                    SubjectTime(
                        subjectTimeId = 7,
                        subjectId = 7,
                        teacherId = 12,
                        groupId = 3,

                        lessonStart = LocalDateTime.of(2025, Month.JUNE, 26, 9, 30),  // 26 июня (чт)
                        lessonEnd = LocalDateTime.of(2025, Month.JUNE, 26, 11, 0)
                    ),
                    SubjectTime(
                        subjectTimeId = 8,
                        subjectId = 8,
                        teacherId = 10,
                        groupId = 4,
                        lessonStart = LocalDateTime.of(2025, Month.JUNE, 26, 11, 15), // Перемена 15 мин
                        lessonEnd = LocalDateTime.of(2025, Month.JUNE, 26, 12, 45)
                    ),
                )
                db.subjectsTimeDao().insertAll(monday)
                db.subjectsTimeDao().insertAll(tuesday)
                db.subjectsTimeDao().insertAll(wednesday)
                db.subjectsTimeDao().insertAll(thursday)

            }

            if(db.gradeDao().count() == 0){

                val marks = listOf(
                    Grade(
                        gradeId = 1,
                        subjectId = 1,
                        studentId = 2,
                        value = 5,

                        date = LocalDate.of(2025, Month.JUNE, 23).toString()
                    ),
                    Grade(
                        gradeId = 1,
                        subjectId = 1,
                        studentId = 2,
                        value = 5,
                        date = LocalDate.of(2025, Month.JUNE, 23).toString()
                    ),
                    Grade(
                        gradeId = 1,
                        subjectId = 1,
                        studentId = 2,
                        value = 5,
                        date = LocalDate.of(2025, Month.JUNE, 23).toString()
                    ),
                )
                db.gradeDao().insertAll(marks)
            }
        }

    }
}

