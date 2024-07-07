package com.example.safekeys.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Credential (
    val username: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val password: ByteArray,
    val website: String,
    val title: String,
    val dateCreated: LocalDateTime,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val iv: ByteArray,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

class InvalidCredentialException(message: String): Exception(message)