package com.example.pivco_fragments

import com.example.pivco_fragments.Models.Character
import com.example.pivco_fragments.Models.CharacterEntity

object AppMapper {
    fun Character.toCharacterEntity(): CharacterEntity {
        return CharacterEntity(
            name = this.name,
            culture = this.culture,
            born = this.born,
            titles = this.titles,
            aliases = this.aliases,
            playedBy = this.playedBy
        )
    }

    fun CharacterEntity.toCharacter(): Character {
        return Character(
            name = this.name,
            culture = this.culture,
            born = this.born,
            titles = this.titles,
            aliases = this.aliases,
            playedBy = this.playedBy
        )
    }
}