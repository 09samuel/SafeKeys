package com.example.safekeys.di

import android.app.Application
import androidx.room.Room
import com.example.safekeys.data.local.AppDatabase
import com.example.safekeys.data.local.CredentialDao
import com.example.safekeys.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestDatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(appContext: Application): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            appContext,
            AppDatabase::class.java,
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao
    }

    @Provides
    @Singleton
    fun provideCredentialDao(appDatabase: AppDatabase): CredentialDao {
        return appDatabase.credentialDao
    }
}
