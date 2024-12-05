package com.example.pivco_fragments.Entity

import com.example.pivco_fragments.Data.Character

object CharacterMapper {
    fun CharacterEntity.toCharacter(): Character {
        return com.example.pivco_fragments.Data.Character(
            name = this.name,
            culture = this.culture,
            born = this.born,
            titles = this.titles,
            aliases = this.aliases,
            playedBy = this.playedBy
        )
    }

    fun List<CharacterEntity>.toCharacterList(): List<Character> {
        return this.map { it.toCharacter() }
    }
}
