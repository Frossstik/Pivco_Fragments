package com.example.pivco_fragments.Models

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    var name: String,
    val culture: String? = null,
    val born: String? = null,
    val titles: List<String> = emptyList(),
    val aliases: List<String> = emptyList(),
    val playedBy: List<String> = emptyList()
)


