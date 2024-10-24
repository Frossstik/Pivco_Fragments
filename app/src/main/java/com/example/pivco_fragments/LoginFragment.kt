package com.example.pivco_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import com.example.pivco_fragments.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonEnter.setOnClickListener {
            startHomeFragment()
        }
    }

    private fun startHomeFragment() {
        val mailText: String = binding.editTextTextEmailAddress.text.toString()
        val passwordText: String = binding.editTextNumberPassword.text.toString()

        if (mailText == "mail@gmail.com" && passwordText == "11111111") {
            val bundle = Bundle().apply {
                putString("mail", mailText)
            }

            val homeFragment = HomeFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, homeFragment)
                .addToBackStack(null)
                .commit()
        } else {
            binding.incorrect.text = "Неверный email или пароль!"
        }
    }
}





