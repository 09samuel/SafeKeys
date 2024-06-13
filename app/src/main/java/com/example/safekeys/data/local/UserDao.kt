package com.example.safekeys.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.safekeys.data.model.User

@Dao
interface UserDao {
    @Upsert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE id = 1")
    fun getUser(): User?
}