package com.example.safekeys

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database (entities = [User::class, Credential::class], version = 1, exportSchema= false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val userDao: UserDao
    abstract val credentialDao: CredentialDao
}