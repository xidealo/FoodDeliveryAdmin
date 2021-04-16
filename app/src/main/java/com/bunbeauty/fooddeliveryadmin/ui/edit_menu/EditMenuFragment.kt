package com.bunbeauty.fooddeliveryadmin.ui.edit_menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentEditMenuBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.adapter.MenuProductsAdapter
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.presentation.view_model.EditMenuViewModel
import com.bunbeauty.presentation.view_model.LoginViewModel
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class EditMenuFragment : BaseFragment<FragmentEditMenuBinding>() {

    override var layoutId = R.layout.fragment_edit_menu
    override val viewModel: EditMenuViewModel by viewModels { modelFactory }

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
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
    }

    fun goToEditMenuProduct(menuProduct: MenuProduct) {
        findNavController().navigate(
            EditMenuFragmentDirections.toEditMenuProductFragment(
                menuProduct
            )
        )
    }
}