package com.bunbeauty.fooddeliveryadmin.ui.edit_menu

import android.os.Bundle
import android.view.View
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentEditMenuProductBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.presentation.view_model.EditMenuProductViewModel

class EditMenuProductFragment :
    BaseFragment<FragmentEditMenuProductBinding, EditMenuProductViewModel>() {

    override var viewModelVariable: Int = BR.viewModel
    override var layoutId: Int = R.layout.fragment_edit_menu_product
    override var viewModelClass = EditMenuProductViewModel::class.java

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuProduct = EditMenuProductFragmentArgs.fromBundle(requireArguments()).menuProduct
        viewDataBinding.menuProduct = menuProduct

        viewDataBinding.fragmentEditMenuProductBtnUpdate.setOnClickListener {
            menuProduct.visible = viewDataBinding.fragmentEditMenuProductRbShow.isChecked
            viewModel.updateMenuProduct(menuProduct)
        }
    }


}