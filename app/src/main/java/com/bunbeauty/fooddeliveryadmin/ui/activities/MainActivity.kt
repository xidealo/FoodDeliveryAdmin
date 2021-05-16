package com.bunbeauty.fooddeliveryadmin.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bunbeauty.common.extensions.toggleVisibility
import com.bunbeauty.fooddeliveryadmin.FoodDeliveryAdminApplication
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.Router
import com.bunbeauty.fooddeliveryadmin.databinding.ActivityMainBinding
import com.bunbeauty.fooddeliveryadmin.presentation.MainViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    lateinit var viewDataBinding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var modelFactory: ViewModelFactory

    @Inject
    lateinit var router: Router

    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as FoodDeliveryAdminApplication).appComponent
            .getViewModelComponent()
            .create(this)
            .inject(this)

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.executePendingBindings()

        viewModel = ViewModelProvider(this, modelFactory).get(MainViewModel::class.java)
        viewModel.refreshCafeList()
        viewModel.refreshProductList()
        setupBottomNavigationBar()

        router.attach(this, R.id.activity_main_fcv_container)
    }

    private fun setupBottomNavigationBar() {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.activity_main_fcv_container) as NavHostFragment).findNavController()
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.ordersFragment, R.id.statisticFragment, R.id.editMenuFragment)
        )

        viewDataBinding.activityMainBnvBottomNavigationBar.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.ordersFragment, R.id.statisticFragment, R.id.editMenuFragment ->
                    toggleBottomNavigationBarVisibility(true)
                else -> toggleBottomNavigationBarVisibility(false)
            }
        }
    }

    private fun toggleBottomNavigationBarVisibility(isVisible: Boolean) {
        viewDataBinding.activityMainBnvBottomNavigationBar.toggleVisibility(isVisible)
    }
}