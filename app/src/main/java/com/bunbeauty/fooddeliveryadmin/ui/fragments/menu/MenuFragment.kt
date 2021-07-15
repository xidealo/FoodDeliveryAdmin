package com.bunbeauty.fooddeliveryadmin.ui.fragments.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentMenuBinding
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.fooddeliveryadmin.ui.items.MenuProductItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.presentation.navigation_event.MenuNavigationEvent
import com.bunbeauty.fooddeliveryadmin.ui.fragments.menu.MenuFragmentDirections.*
import com.bunbeauty.presentation.state.State
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>() {

    override val viewModel: com.bunbeauty.presentation.view_model.menu.MenuViewModel by viewModels()

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
                is State.Loading -> Unit
                is State.Success -> {
                    val items = productListState.data.map{ menuProductItemModel ->
                        MenuProductItem(menuProductItemModel)
                    }
                    itemAdapter.set(items)
                }
                else -> Unit
            }
        }.startedLaunch(viewLifecycleOwner)
        viewModel.navigation.onEach { navigationEvent ->
            when (navigationEvent) {
                is MenuNavigationEvent.ToCreateMenuProduct ->
                    router.navigate(toCreateMenuProductFragment())
                is MenuNavigationEvent.ToEditMenuProduct ->
                    router.navigate(toEditMenuProductFragment(navigationEvent.menuProduct))
                else -> Unit
            }
        }.startedLaunch(viewLifecycleOwner)
    }
}