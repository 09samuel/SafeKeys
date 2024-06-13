package com.example.safekeys.di

import com.example.safekeys.data.repository.CredentialRepositoryImpl
import com.example.safekeys.data.repository.UserRepositoryImpl
import com.example.safekeys.domain.repository.CredentialRepository
import com.example.safekeys.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindCredentialRepository(
        credentialRepositoryImpl: CredentialRepositoryImpl
    ): CredentialRepository

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

}