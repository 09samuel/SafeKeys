package com.example.safekeys.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.safekeys.data.model.Credential
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

    @Query("SELECT * FROM credential WHERE LOWER(title) = LOWER(:name) OR LOWER(website) = LOWER(:name)")
    fun getCredentialsForAutofill(name: String): List<Credential>

}