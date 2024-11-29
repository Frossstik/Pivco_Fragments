package com.example.pivco_fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pivco_fragments.Models.Person
import com.example.pivco_fragments.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val basePerson = Person("mail@gmail.com", "11111111")
        Person.list.add(basePerson)

    }

}
