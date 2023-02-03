package com.bunbeauty.fooddeliveryadmin.screen.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentMenuBinding
import com.bunbeauty.fooddeliveryadmin.util.startedLaunch
import com.bunbeauty.presentation.state.State
import com.bunbeauty.presentation.view_model.menu.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>() {

    override val viewModel: MenuViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
        }

        viewModel.productListState.onEach { productListState ->
            when (productListState) {
                is State.Loading -> {
                }
                is State.Success -> {
                }
                else -> Unit
            }
        }.startedLaunch(viewLifecycleOwner)
    }
}
