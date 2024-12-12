package com.example.pivco_fragments.Ktor

import com.example.pivco_fragments.Models.Character

interface KtorNetworkApi {
    suspend fun getCharacters(page: Int): List<Character>

    fun close()
}