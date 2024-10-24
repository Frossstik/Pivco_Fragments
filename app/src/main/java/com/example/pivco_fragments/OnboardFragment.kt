package com.example.pivco_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pivco_fragments.databinding.FragmentOnboardBinding

class OnboardFragment : Fragment() {

    private var _binding: FragmentOnboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonEnter.setOnClickListener {
            startLoginFragment()
        }

        binding.buttonRegistration.setOnClickListener {
            startRegisterFragment()
        }
    }

    private fun startLoginFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LoginFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun startRegisterFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, RegisterFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}