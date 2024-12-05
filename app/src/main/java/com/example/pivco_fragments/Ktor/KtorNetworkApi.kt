package com.example.pivco_fragments.Ktor

import com.example.pivco_fragments.Data.Character

interface KtorNetworkApi {
    suspend fun getCharacters(): List<Character>
}