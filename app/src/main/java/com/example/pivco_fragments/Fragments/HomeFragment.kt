package com.example.pivco_fragments.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pivco_fragments.AppMapper.toCharacter
import com.example.pivco_fragments.Database.AppDatabase
import com.example.pivco_fragments.Database.CharacterDao
import com.example.pivco_fragments.Database.CharacterRepository
import com.example.pivco_fragments.Ktor.CharactersAdapter
import com.example.pivco_fragments.Ktor.KtorNetworkApi
import com.example.pivco_fragments.Ktor.KtorNetwork
import com.example.pivco_fragments.R
import com.example.pivco_fragments.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import kotlin.math.log

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding ?: RuntimeException(":(") as FragmentHomeBinding

    private lateinit var ktorApi: KtorNetworkApi

    private lateinit var database: AppDatabase
    private lateinit var characterDao: CharacterDao
    private lateinit var repository: CharacterRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }

        binding.userList.itemAnimator = null

        val args: HomeFragmentArgs by navArgs()

        val adapter = CharactersAdapter(emptyList())
        binding.userList.adapter = adapter
        binding.userList.layoutManager = LinearLayoutManager(requireContext())

        database = AppDatabase.getInstance(requireContext())
        characterDao = database.characterDao()
        repository = CharacterRepository(characterDao)
        ktorApi = KtorNetwork()

        lifecycleScope.launch {
            characterDao.getAllCharactersFlow().collect { characterEntities ->
                val characters = characterEntities.map { it.toCharacter() }
                adapter.updateData(characters)
            }
        }

        lifecycleScope.launch {
            val localCharacters = repository.getAllCharacters()
            if (localCharacters.isEmpty()) {
                fetchAndSaveCharacters(6)
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                val localCharacters = repository.getAllCharacters()
                if (localCharacters.isEmpty()) {
                    fetchAndSaveCharacters(6)
                }
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        binding.uploadButton.setOnClickListener {
            val number = binding.numberInput.text.toString().toIntOrNull() ?: 1
            lifecycleScope.launch {
                repository.deleteAllCharacters()
                fetchAndSaveCharacters(number)
            }
        }

        binding.clearButton.setOnClickListener {
            lifecycleScope.launch {
                repository.deleteAllCharacters()
                adapter.updateData(emptyList())
            }
        }

        val person = args.personObject

        if (person != null) {
            binding.yourMail.text = person.mail
        }
        binding.logout.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.onboardFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_homeFragment_to_onboardFragment,
                null,
                navOptions
            )
        }



    }

    private suspend fun fetchAndSaveCharacters(page: Int) {
        val characters = ktorApi.getCharacters(page)
        repository.insertAllCharacters(characters)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        ktorApi.close()

        _binding = null
    }

}