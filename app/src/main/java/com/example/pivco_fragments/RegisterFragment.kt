package com.example.pivco_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pivco_fragments.databinding.FragmentRegisterBinding // Импортируйте сгенерированный класс View Binding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

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
            startHomeFragment()
        }
    }

    private fun startHomeFragment() {
        val passwordText1 = binding.editTextNumberPassword.text.toString()
        val passwordText2 = binding.editTextNumberPassword2.text.toString()

        if (passwordText1 == passwordText2) {
            val mailText = binding.editTextTextEmailAddress.text.toString()

            val bundle = Bundle()
            bundle.putString("mail", mailText)

            val homeFragment = HomeFragment()
            homeFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, homeFragment)
                .addToBackStack(null)
                .commit()
        } else {
            binding.incorrect.text = "Пароли не совпадают!"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
