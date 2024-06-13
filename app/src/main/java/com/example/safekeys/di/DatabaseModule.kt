package com.example.safekeys.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.safekeys.data.local.AppDatabase
import com.example.safekeys.data.local.CredentialDao
import com.example.safekeys.data.local.UserDao
import com.example.safekeys.data.model.Credential
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(appContext: Application): AppDatabase{
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "safekeys.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao{
        return appDatabase.userDao
    }

    @Provides
    @Singleton
    fun provideCredentialDao(appDatabase: AppDatabase): CredentialDao {
        return appDatabase.credentialDao
    }
}