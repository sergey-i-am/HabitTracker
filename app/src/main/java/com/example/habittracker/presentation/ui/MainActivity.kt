package com.example.habittracker.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.habittracker.R
import com.example.habittracker.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private var currentFragment: Fragment? = null

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fragmentManager: FragmentManager,
            fragment: Fragment,
            view: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fragmentManager, fragment, view, savedInstanceState)
            if (fragment is NavHostFragment) return
            currentFragment = fragment
            updateTitle()
        }
    }

    private fun updateTitle() {
        val fragment = currentFragment
        if (fragment is HasTitle) {
            binding.toolbar.title = getString(fragment.getTitleResId())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupNavigation()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navigationView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }
}