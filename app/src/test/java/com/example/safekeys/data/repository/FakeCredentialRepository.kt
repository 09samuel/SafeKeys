package com.example.safekeys.data.repository

import com.example.safekeys.data.model.Credential
import com.example.safekeys.domain.repository.CredentialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCredentialRepository: CredentialRepository {

    private val credentials = mutableListOf<Credential>()
    override fun getCredentialsForAutofill(name: String): List<Credential> {
        return credentials.filter { it.title == name }
    }

    override suspend fun upsertCredential(credential: Credential) {
        credentials.add(credential)
    }

    override suspend fun deleteCredential(credential: Credential) {
        credentials.remove(credential)
    }

    override fun getCredentialsOrderedByTitle(): Flow<List<Credential>> {
        return flowOf(credentials.sortedBy { it.title })
    }

    override fun getCredentialsOrderedByDateCreated(): Flow<List<Credential>> {
        return flowOf(credentials.sortedBy { it.dateCreated })
    }
}