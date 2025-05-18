package com.example.collage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.collage.dao.GroupDao
import com.example.collage.dao.UserDao
import com.example.collage.models.Group
import com.example.collage.models.Role
import com.example.collage.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        Group::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun userDao(): UserDao

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
                    User(userId = 2, name = "Иванов Иван Иванович", role = Role.STUDENT, phone = "87775554433", groupId = 2),
                    User(userId = 3, name = "Петрова Анна Сергеевна", role = Role.STUDENT, phone = "87011234567", groupId = 3),
                    User(userId = 4, name = "Смирнов Алексей Дмитриевич", role = Role.STUDENT, phone = "87778889900",groupId = 4),
                    User(userId = 5, name = "Кузнецова Екатерина Викторовна", role = Role.STUDENT, phone = "87022345678",groupId = 5),
                    User(userId = 6, name = "Григорьев Денис Олегович", role = Role.STUDENT, phone = "87771231234", groupId = 6)
                )
                db.userDao().insertAll(usersWithGroup)

            }
        }

    }
}

