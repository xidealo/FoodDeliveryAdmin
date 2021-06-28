package com.bunbeauty.fooddeliveryadmin.ui.fragments.menu

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentMenuBinding
import com.bunbeauty.fooddeliveryadmin.extensions.launchWhenStarted
import com.bunbeauty.fooddeliveryadmin.presentation.menu.MenuViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.ui.adapter.MenuProductsAdapter
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.MenuProductItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>() {

    override val viewModel: MenuViewModel by viewModels()

    @Inject
    lateinit var menuProductsAdapter: MenuProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemAdapter = ItemAdapter<MenuProductItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        viewDataBinding.fragmentMenuRvList.adapter = fastAdapter
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
        }.launchWhenStarted(lifecycleScope)

        viewDataBinding.fragmentMenuFabCreateProduct.setOnClickListener {
            viewModel.goToCreateMenuProduct()
        }
    }
}