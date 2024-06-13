package com.example.safekeys.data.repository

import com.example.safekeys.data.local.CredentialDao
import com.example.safekeys.data.model.Credential
import com.example.safekeys.domain.repository.CredentialRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CredentialRepositoryImpl @Inject constructor(private val credentialDao: CredentialDao): CredentialRepository {
    override fun getCredentialsForAutofill(name: String): List<Credential> {
        return credentialDao.getCredentialsForAutofill(name)
    }

    override suspend fun upsertCredential(credential: Credential) {
        credentialDao.upsertCredential(credential)
    }

    override suspend fun deleteCredential(credential: Credential) {
        credentialDao.deleteCredential(credential)
    }

    override fun getCredentialsOrderedByTitle(): Flow<List<Credential>> {
        return credentialDao.getCredentialsOrderedByTitle()
    }

    override fun getCredentialsOrderedByDateCreated(): Flow<List<Credential>> {
        return credentialDao.getCredentialsOrderedByDateCreated()
    }
}