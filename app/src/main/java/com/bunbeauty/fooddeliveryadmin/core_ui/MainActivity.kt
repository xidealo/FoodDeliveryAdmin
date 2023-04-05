package com.bunbeauty.fooddeliveryadmin.core_ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
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

    private val requestPermissionLauncher by lazy {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkNotificationPermission()
        setupBottomNavigationBar()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
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
