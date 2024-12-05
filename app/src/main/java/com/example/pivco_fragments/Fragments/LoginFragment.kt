package com.example.pivco_fragments.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.pivco_fragments.Data.Person
import com.example.pivco_fragments.R
import com.example.pivco_fragments.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding ?: RuntimeException(":(") as FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonEnter.setOnClickListener {
            val mailText: String = binding.editTextTextEmailAddress.text.toString()
            val passwordText: String = binding.editTextNumberPassword.text.toString()
            var tempBool : Boolean = false
            for (person in Person.list) {
                if (mailText == person.mail && passwordText == person.password) {
                    /*val bundle = Bundle()
                    bundle.putSerializable("person", person)
                    findNavController().navigate(
                        R.id.action_loginFragment_to_homeFragment,
                        bundle,
                        NavOptions.Builder().setPopUpTo(R.id.onboardFragment, true).build()
                    )
                    tempBool = true
                    break*/
                    val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment(person)
                    findNavController().navigate(action, NavOptions.Builder().setPopUpTo(R.id.onboardFragment, true).build())
                }
            }
            if (!tempBool)
                binding.incorrect.text = "Пароли не совпадают!"
        }
    }

}





