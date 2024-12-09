package com.example.pivco_fragments.Ktor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pivco_fragments.Models.Character
import com.example.pivco_fragments.databinding.ListCharactersBinding

class CharactersAdapter(private var characters: List<Character>) :
    RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ListCharactersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    override fun getItemCount() = characters.size

    fun updateData(newCharacters: List<Character>) {
        characters = newCharacters
        notifyDataSetChanged()
    }

    class CharacterViewHolder(private val binding: ListCharactersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            with(binding) {
                name.text = character.name ?: "-"
                culture.text = character.culture ?: "-"
                born.text = character.born ?: "-"
                titles.text = character.titles.joinToString() ?: "-"
                aliases.text = character.aliases.joinToString() ?: "-"
                playedBy.text = character.playedBy.joinToString() ?: "-"
            }
        }
    }
}