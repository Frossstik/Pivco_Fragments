package com.example.pivco_fragments.Entity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterViewModel(private val repository: CharacterRepository) : ViewModel() {

    val characters = repository.getCharacters()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun refreshCharacters() {
        viewModelScope.launch {
            repository.fetchAndSaveCharacters()
        }
    }


}