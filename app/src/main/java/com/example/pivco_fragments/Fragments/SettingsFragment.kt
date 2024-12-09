package com.example.pivco_fragments.Fragments

import android.content.Context
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.pivco_fragments.databinding.FragmentSettingsBinding
import java.io.File
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import com.example.pivco_fragments.Ktor.KtorNetwork
import kotlinx.coroutines.launch
import java.io.*
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.pivco_fragments.Database.AppDatabase
import com.example.pivco_fragments.Database.CharacterDao
import com.example.pivco_fragments.Database.CharacterRepository
import com.example.pivco_fragments.Ktor.KtorNetworkApi
import com.example.pivco_fragments.R
import kotlinx.coroutines.flow.map

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding  ?: RuntimeException(":(") as FragmentSettingsBinding

    private val LANGUAGE_KEY = stringPreferencesKey("language_key")
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDarkModeSwitch()
        setupFileManagementButtons()
        setupLanguageSelector()
    }

    private fun setupDarkModeSwitch() {
        val sharedPreferences =
            requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        binding.switchDarkMode.isChecked = sharedPreferences.getBoolean("dark_mode", false)

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
        }
    }

    private fun setupLanguageSelector() {
        val sharedPreferences = requireContext().dataStore

        lifecycleScope.launch {
            sharedPreferences.data.map { preferences ->
                preferences[LANGUAGE_KEY] ?: "en"
            }.collect { savedLanguage ->
                val index = resources.getStringArray(R.array.language_values).indexOf(savedLanguage)
                binding.spinnerLanguage.setSelection(index)
            }
        }

        binding.spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguage = resources.getStringArray(R.array.language_values)[position]
                lifecycleScope.launch {
                    sharedPreferences.edit { settings ->
                        settings[LANGUAGE_KEY] = selectedLanguage
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupFileManagementButtons() {
        binding.buttonExportFile.setOnClickListener {
            exportFile()
        }

        binding.buttonDeleteFile.setOnClickListener {
            deleteFile()
        }

        binding.buttonRestoreFile.setOnClickListener {
            restoreFile()
        }
    }

    private fun exportFile() {
        lifecycleScope.launch {
            val database = AppDatabase.getInstance(requireContext())
            val repository = CharacterRepository(database.characterDao())

            val characters = repository.getAllCharacters()

            if (characters.isNotEmpty()) {
                val fileName = "Characters.txt"
                val externalFile = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    fileName
                )

                try {
                    FileOutputStream(externalFile).use { outputStream ->
                        outputStream.write("Список персонажей:\n".toByteArray())

                        characters.forEach { character ->
                            val characterInfo = """
                            Имя: ${character.name}
                            Культура: ${character.culture ?: "-"}
                            Родился: ${character.born ?: "-"}
                            Титулы: ${if (character.titles.isNotEmpty()) character.titles.joinToString(", ") else "-"}
                            Псевдонимы: ${if (character.aliases.isNotEmpty()) character.aliases.joinToString(", ") else "-"}
                            Играл(а): ${if (character.playedBy.isNotEmpty()) character.playedBy.joinToString(", ") else "-"}
                        """.trimIndent() + "\n\n"

                            outputStream.write(characterInfo.toByteArray())
                        }
                    }
                    Toast.makeText(
                        requireContext(),
                        "Файл экспортирован в ${externalFile.path}",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Ошибка экспорта файла", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Нет данных для экспорта", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }



    private fun deleteFile() {
        val fileName = "Characters.txt"
        val externalFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            fileName
        )
        val internalFile = File(requireContext().filesDir, fileName)

        if (externalFile.exists()) {
            try {
                copyFile(externalFile, internalFile)
                externalFile.delete()
                Toast.makeText(
                    requireContext(),
                    "Файл удален и сохранен в резервной копии",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Ошибка удаления файла", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Файл не найден", Toast.LENGTH_SHORT).show()
        }
    }

    private fun restoreFile() {
        val fileName = "Characters.txt"
        val internalFile = File(requireContext().filesDir, fileName)
        val externalFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            fileName
        )

        if (internalFile.exists()) {
            try {
                copyFile(internalFile, externalFile)
                Toast.makeText(requireContext(), "Файл восстановлен", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Ошибка восстановления файла", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(requireContext(), "Резервная копия не найдена", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun copyFile(sourceFile: File, destFile: File) {
        FileInputStream(sourceFile).use { input ->
            FileOutputStream(destFile).use { output ->
                input.copyTo(output)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}