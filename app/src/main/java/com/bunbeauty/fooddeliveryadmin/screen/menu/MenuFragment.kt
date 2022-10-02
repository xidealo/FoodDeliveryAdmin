package com.bunbeauty.fooddeliveryadmin.screen.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentMenuBinding
import com.bunbeauty.fooddeliveryadmin.util.invisible
import com.bunbeauty.fooddeliveryadmin.util.startedLaunch
import com.bunbeauty.fooddeliveryadmin.util.visible
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.presentation.navigation_event.MenuNavigationEvent
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
                    binding.fragmentMenuLpiLoading.visible()
                }
                is State.Success -> {
                    val items = productListState.data.map { menuProductItemModel ->
                        MenuProductItem(menuProductItemModel)
                    }
                    itemAdapter.set(items)
                    binding.fragmentMenuLpiLoading.invisible()
                }
                else -> Unit
            }
        }.startedLaunch(viewLifecycleOwner)

        viewModel.navigation.onEach { navigationEvent ->
            when (navigationEvent) {
                is MenuNavigationEvent.ToCreateMenuProduct ->
                    router.navigate(MenuFragmentDirections.toCreateMenuProductFragment())
                is MenuNavigationEvent.ToEditMenuProduct ->
                    router.navigate(MenuFragmentDirections.toEditMenuProductFragment(navigationEvent.menuProduct))
                else -> Unit
            }
        }.startedLaunch(viewLifecycleOwner)
    }
}