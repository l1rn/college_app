package com.example.collage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.collage.models.Role
import com.example.collage.models.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertAll(users: List<User>)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int

    @Query("SELECT EXISTS(SELECT * FROM users WHERE phone = :phone)")
    suspend fun isUserExists(phone: String): Boolean

    @Query("SELECT role FROM users WHERE :phone = phone")
    suspend fun getRoleFromPhone(phone: String): Role

    @Query("SELECT userId FROM users WHERE :phone = phone")
    suspend fun getUserIdFromPhone(phone: String): Int

}