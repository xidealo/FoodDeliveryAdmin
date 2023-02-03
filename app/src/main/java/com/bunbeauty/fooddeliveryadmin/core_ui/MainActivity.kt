package com.bunbeauty.fooddeliveryadmin.core_ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ActivityMainBinding
import com.bunbeauty.presentation.view_model.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigationBar()
    }

    @SuppressLint("RestrictedApi")
    private fun setupBottomNavigationBar() {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.activity_main_fcv_container) as NavHostFragment).findNavController()

        binding.activityMainBnvBottomNavigationBar.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.ordersFragment, R.id.statisticFragment, R.id.menuFragment -> {
                    toggleBottomNavigationBarVisibility(true)
                    val countDestination = navController.backQueue.count { navBackStackEntry ->
                        navBackStackEntry.destination.id == destination.id
                    }
                    if (countDestination > 1) {
                        navController.popBackStack(destination.id, true)
                        navController.popBackStack(destination.id, false)
                    }
                }
                else -> toggleBottomNavigationBarVisibility(false)
            }
        }
        binding.activityMainBnvBottomNavigationBar.setOnItemReselectedListener { }
    }

    private fun toggleBottomNavigationBarVisibility(isVisible: Boolean) {
        binding.activityMainBnvBottomNavigationBar.isVisible = isVisible
    }
}
