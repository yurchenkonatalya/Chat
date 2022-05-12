package com.example.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chat.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

const val BASE_IMAGE_URL = "https://xaltura.by/chat/resources/userphotos/"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController =
            (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
        binding.bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val destinationId = destination.id
            binding.bottomNavigation.isVisible =
                destinationId == R.id.nav_dialogs_fragment ||
                        destinationId == R.id.nav_search_users_fragment ||
                        destinationId == R.id.nav_settings_fragment
        }
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.nav_dialogs_fragment || navController.currentDestination?.id == R.id.nav_signin_fragment)
            finish()
        else
            navController.popBackStack()
    }
}