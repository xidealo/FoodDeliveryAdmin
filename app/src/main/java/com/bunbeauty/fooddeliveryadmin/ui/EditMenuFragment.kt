package com.bunbeauty.fooddeliveryadmin.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.view.menu.MenuAdapter
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentEditMenuBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.adapter.MenuProductsAdapter
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.presentation.view_model.EditMenuViewModel
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class EditMenuFragment : BaseFragment<FragmentEditMenuBinding, EditMenuViewModel>() {

    override var viewModelVariable: Int = BR.viewModel
    override var layoutId: Int = R.layout.fragment_edit_menu
    override var viewModelClass = EditMenuViewModel::class.java

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var menuProductsAdapter: MenuProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.fragmentEditMenuRvResult.adapter = menuProductsAdapter
        viewModel.getProducts()

        viewModel.productListSharedFlow.onEach {
            menuProductsAdapter.setItemList(it)
        }.launchWhenStarted(lifecycleScope)
    }

}