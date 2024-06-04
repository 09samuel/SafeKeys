package com.example.safekeys

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CredentialDao {
    @Upsert
    suspend fun upsertCredential(credential: Credential)

    @Delete
    suspend fun deleteCredential(credential: Credential)

    @Query("SELECT * FROM credential ORDER BY title ASC")
    fun getCredentialsOrderedByTitle(): Flow<List<Credential>>

    @Query("SELECT * FROM credential ORDER BY dateCreated ASC")
    fun getCredentialsOrderedByDateCreated(): Flow<List<Credential>>
}