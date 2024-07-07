package com.example.safekeys.di

import android.app.Application
import android.content.Context
import com.example.safekeys.domain.usecase.ValidateConfirmPassword
import com.example.safekeys.domain.usecase.ValidatePassword
import com.example.safekeys.utils.CryptoManager
import com.example.safekeys.utils.NotificationHelper
import com.example.safekeys.utils.SharedPreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideValidatePassword(): ValidatePassword {
        return ValidatePassword()
    }

    @Provides
    @Singleton
    fun provideValidateConfirmPassword(): ValidateConfirmPassword {
        return ValidateConfirmPassword()
    }

    @Provides
    @Singleton
    fun provideCryptoManager(): CryptoManager {
        return CryptoManager()
    }

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideNotificationHelper(context: Context): NotificationHelper {
        return NotificationHelper(context)
    }

}