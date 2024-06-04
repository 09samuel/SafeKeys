package com.example.safekeys

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDao {
    @Upsert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE id = 1")
    fun getUser(): User?

}