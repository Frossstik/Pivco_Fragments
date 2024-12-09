package com.example.pivco_fragments.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val culture: String? = null,
    val born: String? = null,
    val titles: List<String> = emptyList(),
    val aliases: List<String> = emptyList(),
    val playedBy: List<String> = emptyList()
)
