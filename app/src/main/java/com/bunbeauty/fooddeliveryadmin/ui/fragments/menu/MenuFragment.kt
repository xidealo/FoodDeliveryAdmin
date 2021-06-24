package com.bunbeauty.fooddeliveryadmin.ui.fragments.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentEditMenuBinding
import com.bunbeauty.fooddeliveryadmin.presentation.menu.MenuViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.MenuProductsAdapter
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentEditMenuBinding, MenuViewModel>() {

    override val viewModel: MenuViewModel by viewModels()

    @Inject
    lateinit var menuProductsAdapter: MenuProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* menuProductsAdapter.onItemClickListener = {
             goToEditMenuProduct(it)
         }
         viewDataBinding.fragmentEditMenuRvResult.adapter = menuProductsAdapter
         viewModel.getProducts()

         viewModel.productListSharedFlow.onEach {
             menuProductsAdapter.setItemList(it)
         }.launchWhenStarted(lifecycleScope)*/

        viewDataBinding.fragmentEditMenuFabAddNewProduct.setOnClickListener {
            goToCreateMenuProduct()
        }
    }

    private fun goToEditMenuProduct(menuProduct: MenuProduct) {
        //router.navigate(toEditMenuProductFragment(menuProduct))
    }

    private fun goToCreateMenuProduct() {
        //router.navigate(toCreateNewMenuProductFragment(viewModel.productListSharedFlow.value.getNewMenuUuid()))
    }
}