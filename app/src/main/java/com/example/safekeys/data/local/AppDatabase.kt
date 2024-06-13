package com.example.safekeys.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.safekeys.data.model.Credential
import com.example.safekeys.data.model.User
import com.example.safekeys.utils.Converters

@Database (entities = [User::class, Credential::class], version = 1, exportSchema= false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val userDao: UserDao
    abstract val credentialDao: CredentialDao
}