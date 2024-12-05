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
import com.example.pivco_fragments.databinding.FragmentRegisterBinding // Импортируйте сгенерированный класс View Binding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding ?: RuntimeException(":(") as FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonEnter.setOnClickListener {
            val mailText = binding.editTextTextEmailAddress.text.toString()
            val passwordText1 = binding.editTextNumberPassword.text.toString()
            val passwordText2 = binding.editTextNumberPassword2.text.toString()

            if (passwordText1 == passwordText2) {

                /*val newPerson = Person(mailText, passwordText1)

                Person.list.add(newPerson)

                val bundle = Bundle()
                bundle.putSerializable("person", newPerson)

                findNavController().navigate(
                    R.id.action_registerFragment_to_homeFragment,
                    bundle,
                    NavOptions.Builder().setPopUpTo(R.id.onboardFragment, true).build()
                )*/
                val person = Person(mailText, passwordText1)

                Person.list.add(person)
                val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment(person)
                findNavController().navigate(action, NavOptions.Builder().setPopUpTo(R.id.onboardFragment, true).build())


            } else {
                binding.incorrect.text = "Пароли не совпадают!"
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
