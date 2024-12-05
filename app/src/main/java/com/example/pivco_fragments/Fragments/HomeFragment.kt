package com.example.pivco_fragments.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.pivco_fragments.Entity.AppDatabase
import com.example.pivco_fragments.Entity.CharacterMapper.toCharacter
import com.example.pivco_fragments.Entity.CharacterRepository
import com.example.pivco_fragments.Entity.CharacterViewModel
import com.example.pivco_fragments.Ktor.CharactersAdapter
import com.example.pivco_fragments.Ktor.KtorNetworkApi
import com.example.pivco_fragments.Ktor.KtorNetwork
import com.example.pivco_fragments.R
import com.example.pivco_fragments.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import com.example.pivco_fragments.Entity.CharacterViewModelFactory

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding?: RuntimeException(":(") as FragmentHomeBinding

    private var _ktorApi: KtorNetworkApi? = null
    private val ktorApi get() = _ktorApi?: RuntimeException("Fuck ktor")

    private lateinit var viewModel: CharacterViewModel
    private lateinit var adapter: CharactersAdapter

    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java,
            "characters_database"
        ).build()

        binding.settingsButton.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }

        val characterDao = AppDatabase.getDatabase(requireContext()).characterDao()
        val repository = CharacterRepository(characterDao, KtorNetwork.create())
        viewModel = ViewModelProvider(this, CharacterViewModelFactory(repository))
            .get(CharacterViewModel::class.java)


        adapter = CharactersAdapter(emptyList())
        binding.userList.adapter = adapter
        binding.userList.layoutManager = LinearLayoutManager(requireContext())

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshCharacters()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.userList.itemAnimator = null

        val adapter = CharactersAdapter(emptyList())
        binding.userList.adapter = adapter

        val args: HomeFragmentArgs by navArgs()

        binding.userList.layoutManager = LinearLayoutManager(requireContext())

        _ktorApi = KtorNetwork()

        lifecycleScope.launch {
            val characters = (_ktorApi as KtorNetworkApi).getCharacters()
            binding.userList.adapter = CharactersAdapter(characters)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.characters.collect { characterEntities ->
                // Преобразуем CharacterEntity в Character
                val characters = characterEntities.map { it.toCharacter() }
                adapter.updateData(characters)  // Обновляем данные через updateData
            }
        }

        binding.yourAvatar.setOnClickListener {
            lifecycleScope.launch {
                val characters = (_ktorApi as KtorNetworkApi).getCharacters()
                binding.userList.adapter = CharactersAdapter(characters)
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
            findNavController().navigate(R.id.action_homeFragment_to_onboardFragment, null, navOptions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}