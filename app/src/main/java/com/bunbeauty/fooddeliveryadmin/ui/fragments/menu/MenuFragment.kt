package com.bunbeauty.fooddeliveryadmin.ui.fragments.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentMenuBinding
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.fooddeliveryadmin.presentation.menu.MenuViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.ui.items.MenuProductItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
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
        binding.fragmentMenuRvList.adapter = fastAdapter
        fastAdapter.onClickListener = { _, _, menuProductItem, _ ->
            viewModel.goToEditMenuProduct(menuProductItem.menuProduct)
            false
        }
        viewModel.productListState.onEach { productListState ->
            when (productListState) {
                is State.Loading -> Unit
                is State.Success -> {
                    itemAdapter.set(productListState.data)
                }
                else -> Unit
            }
        }.startedLaunch(viewLifecycleOwner)

        binding.fragmentMenuFabCreateProduct.setOnClickListener {
            viewModel.goToCreateMenuProduct()
        }
    }
}