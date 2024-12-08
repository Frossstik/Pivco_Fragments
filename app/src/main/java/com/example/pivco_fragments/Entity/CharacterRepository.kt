package com.example.pivco_fragments.Entity

import com.example.pivco_fragments.Ktor.KtorNetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CharacterRepository(
    private val characterDao: CharacterDao,
    private val apiService: KtorNetworkApi
) {

    fun getCharacters(): Flow<List<CharacterEntity>> = characterDao.getAllCharacters()

    suspend fun fetchAndSaveCharacters() {
        val charactersFromApi = apiService.getCharacters()
        withContext(Dispatchers.IO) {
            characterDao.clearCharacters()
            characterDao.insertCharacters(charactersFromApi.map {
                CharacterEntity(
                    id = it.name.hashCode(),
                    name = it.name,
                    culture = it.culture,
                    born = it.born,
                    titles = it.titles,
                    aliases = it.aliases,
                    playedBy = it.playedBy
                )
            })
        }
    }
}
