package com.example.pivco_fragments.Database

import com.example.pivco_fragments.AppMapper.toCharacter
import com.example.pivco_fragments.AppMapper.toCharacterEntity
import com.example.pivco_fragments.Models.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterRepository(private val characterDao: CharacterDao) {

    suspend fun getAllCharacters(): List<Character> {
        for (c in characterDao.getAllCharacters()){
            println(c.id)
        }
        return characterDao.getAllCharacters().map { it.toCharacter() }
    }

    fun getAllCharactersFlow(): Flow<List<Character>> {
        return characterDao.getAllCharactersFlow().map { it.map { entity -> entity.toCharacter()  }}
    }

    suspend fun getCharacterById(id: Int): Character? {
        val characterEntity = characterDao.getCharacterById(id)
        return characterEntity?.toCharacter()
    }

    suspend fun insertCharacter(character: Character) {
        val characterEntity = character.toCharacterEntity()
        characterDao.insertCharacter(characterEntity)
    }

    suspend fun insertAllCharacters(characters: List<Character>) {
        val characterEntities = characters.map { it.toCharacterEntity() }
        characterDao.insertAllCharacters(characterEntities)
    }

    suspend fun deleteCharacter(character: Character) {
        val characterEntity = character.toCharacterEntity()
        characterDao.deleteCharacter(characterEntity)
    }

    suspend fun deleteAllCharacters(){
        characterDao.deleteAllCharacters()
    }

    suspend fun updateCharacter(character: Character) {
        val characterEntity = character.toCharacterEntity()
        characterDao.updateCharacter(characterEntity)
    }
}