package com.example.pivco_fragments

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        if (savedInstanceState == null) {
            navigateToOnboard()
        }
    }

    fun navigateToOnboard() {
        replaceFragment(OnboardFragment())

    }

    fun navigateToLogin() {
        replaceFragment(LoginFragment())
    }

    fun navigateToRegister() {
        replaceFragment(RegisterFragment())
    }

    fun navigateToHome(mail: String) {
        val homeFragment = HomeFragment()

        val bundle = Bundle()
        bundle.putString("mail", mail)
        homeFragment.arguments = bundle

        replaceFragment(homeFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        if (currentFragment is HomeFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
