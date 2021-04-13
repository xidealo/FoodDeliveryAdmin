package com.bunbeauty.fooddeliveryadmin.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentMainBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.presentation.view_model.MainViewModel
import com.bunbeauty.presentation.view_model.OrdersViewModel

class MainFragment : BaseFragment<FragmentMainBinding>() {

    override var layoutId = R.layout.fragment_main
    override val viewModel: MainViewModel by viewModels { modelFactory }

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        NavigationUI.setupWithNavController(
            viewDataBinding.fragmentMainBnvNavigationBar,
            (childFragmentManager.findFragmentById(R.id.fragment_main_fcv_container) as NavHostFragment).findNavController()
        )
        viewDataBinding.fragmentMainBnvNavigationBar.setOnNavigationItemReselectedListener {}
    }
}