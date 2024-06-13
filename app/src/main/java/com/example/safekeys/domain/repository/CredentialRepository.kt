package com.example.safekeys.domain.repository

import com.example.safekeys.data.model.Credential
import kotlinx.coroutines.flow.Flow

interface CredentialRepository {
    fun getCredentialsForAutofill(name: String): List<Credential>

    suspend fun upsertCredential(credential: Credential)
    suspend fun deleteCredential(credential: Credential)
    fun getCredentialsOrderedByTitle(): Flow<List<Credential>>

    fun getCredentialsOrderedByDateCreated(): Flow<List<Credential>>
}