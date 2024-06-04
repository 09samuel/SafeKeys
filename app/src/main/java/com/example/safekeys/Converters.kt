package com.example.safekeys

import android.util.Base64
import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }

//    @TypeConverter
//    fun fromByteArray(byteArray: ByteArray): String {
//        return Base64.encodeToString(byteArray, Base64.DEFAULT)
//    }
//
//    @TypeConverter
//    fun toByteArray(string: String): ByteArray {
//        return Base64.decode(string, Base64.DEFAULT)
//    }
}