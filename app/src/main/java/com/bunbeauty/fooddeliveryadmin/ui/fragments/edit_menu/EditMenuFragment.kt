package com.bunbeauty.fooddeliveryadmin.ui.fragments.edit_menu

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.common.extensions.launchWhenStarted
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.data.model.getNewMenuUuid
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentEditMenuBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import com.bunbeauty.fooddeliveryadmin.ui.adapter.MenuProductsAdapter
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.edit_menu.EditMenuFragmentDirections.toCreateNewMenuProductFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.edit_menu.EditMenuFragmentDirections.toEditMenuProductFragment
import com.bunbeauty.fooddeliveryadmin.presentation.EditMenuViewModel
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class EditMenuFragment : BaseFragment<FragmentEditMenuBinding, EditMenuViewModel>() {

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    @Inject
    lateinit var menuProductsAdapter: MenuProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuProductsAdapter.onItemClickListener = {
            goToEditMenuProduct(it)
        }
        viewDataBinding.fragmentEditMenuRvResult.adapter = menuProductsAdapter
        viewModel.getProducts()

        viewModel.productListSharedFlow.onEach {
            menuProductsAdapter.setItemList(it)
        }.launchWhenStarted(lifecycleScope)

        viewDataBinding.fragmentEditMenuFabAddNewProduct.setOnClickListener {
            goToCreateMenuProduct()
        }
    }

    private fun goToEditMenuProduct(menuProduct: MenuProduct) {
        router.navigate(toEditMenuProductFragment(menuProduct))
    }

    private fun goToCreateMenuProduct() {
        router.navigate(toCreateNewMenuProductFragment(viewModel.productListSharedFlow.value.getNewMenuUuid()))
    }
}