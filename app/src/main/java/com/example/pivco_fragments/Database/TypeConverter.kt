package com.example.pivco_fragments.Database

import androidx.room.TypeConverter

object TypeConverter {

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(data: String?): List<String> {
        return data?.split(",")?.map { it.trim() } ?: emptyList()
    }
}