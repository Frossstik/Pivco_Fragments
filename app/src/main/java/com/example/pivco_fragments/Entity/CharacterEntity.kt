package com.example.pivco_fragments.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val culture: String?,
    val born: String?,
    val titles: List<String>,
    val aliases: List<String>,
    val playedBy: List<String>
)
