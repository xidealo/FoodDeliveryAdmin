package com.bunbeauty.fooddeliveryadmin.screen.menu

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentMenuBinding
import com.bunbeauty.fooddeliveryadmin.util.startedLaunch
import com.bunbeauty.presentation.state.State
import com.bunbeauty.presentation.view_model.menu.MenuViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>() {

    override val viewModel: MenuViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemAdapter = ItemAdapter<MenuProductItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)

        binding.run {
            fragmentMenuRvList.adapter = fastAdapter
            fastAdapter.onClickListener = { _, _, menuProductItem, _ ->
                viewModel.goToEditMenuProduct(menuProductItem.menuProductItemModel)
                false
            }
            fragmentMenuFabCreateProduct.setOnClickListener {
                viewModel.goToCreateMenuProduct()
            }
        }

        viewModel.productListState.onEach { productListState ->
            when (productListState) {
                is State.Loading -> {
                    binding.fragmentMenuLpiLoading.isVisible = true
                }
                is State.Success -> {
                    val items = productListState.data.map { menuProductItemModel ->
                        MenuProductItem(menuProductItemModel)
                    }
                    itemAdapter.set(items)
                    binding.fragmentMenuLpiLoading.isVisible = false
                }
                else -> Unit
            }
        }.startedLaunch(viewLifecycleOwner)
    }
}