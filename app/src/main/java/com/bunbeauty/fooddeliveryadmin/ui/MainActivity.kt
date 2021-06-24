package com.bunbeauty.fooddeliveryadmin.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavBackStackEntry
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.Router
import com.bunbeauty.fooddeliveryadmin.databinding.ActivityMainBinding
import com.bunbeauty.fooddeliveryadmin.extensions.toggleVisibility
import com.bunbeauty.fooddeliveryadmin.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router

    val viewModel: MainViewModel by viewModels()

    lateinit var viewDataBinding: ActivityMainBinding

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        router.attach(this, R.id.activity_main_fcv_container)

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.executePendingBindings()

        viewModel.refreshCafeList()
        viewModel.refreshProductList()
        viewModel.refreshDelivery()

        setupBottomNavigationBar()
    }

    @SuppressLint("RestrictedApi")
    private fun setupBottomNavigationBar() {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.activity_main_fcv_container) as NavHostFragment).findNavController()
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.ordersFragment, R.id.statisticFragment, R.id.editMenuFragment)
        )

        viewDataBinding.activityMainBnvBottomNavigationBar.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.ordersFragment, R.id.statisticFragment, R.id.editMenuFragment -> {
                    toggleBottomNavigationBarVisibility(true)

                    val countDestination = navController.backStack.count { navBackStackEntry ->
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
        viewDataBinding.activityMainBnvBottomNavigationBar.setOnNavigationItemReselectedListener {}
    }

    private fun toggleBottomNavigationBarVisibility(isVisible: Boolean) {
        viewDataBinding.activityMainBnvBottomNavigationBar.toggleVisibility(isVisible)
    }
}