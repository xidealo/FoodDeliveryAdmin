package com.bunbeauty.fooddeliveryadmin.ui.edit_menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentEditMenuProductBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.presentation.view_model.EditMenuProductViewModel
import com.bunbeauty.presentation.view_model.MainViewModel

class EditMenuProductFragment :
    BaseFragment<FragmentEditMenuProductBinding>() {

    override var layoutId = R.layout.fragment_edit_menu_product
    override val viewModel: EditMenuProductViewModel by viewModels { modelFactory }

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuProduct = EditMenuProductFragmentArgs.fromBundle(requireArguments()).menuProduct
        viewDataBinding.menuProduct = menuProduct

        viewDataBinding.fragmentEditMenuProductBtnUpdate.setOnClickListener {
            menuProduct.visible = viewDataBinding.fragmentEditMenuProductRbShow.isChecked
            menuProduct.name = viewDataBinding.fragmentEditMenuProductEtName.text.toString()
            menuProduct.cost = viewDataBinding.fragmentEditMenuProductEtCost.text.toString().toInt()
            menuProduct.weight =
                viewDataBinding.fragmentEditMenuProductEtWeight.text.toString().toInt()
            menuProduct.description =
                viewDataBinding.fragmentEditMenuProductEtDescription.text.toString()
            if (viewDataBinding.fragmentEditMenuProductEtDiscountCost.text.toString()
                    .toInt() == 0
            ) {
                menuProduct.discountCost = null
            } else {
                menuProduct.discountCost =
                    viewDataBinding.fragmentEditMenuProductEtDiscountCost.text.toString().toInt()
            }

            viewModel.updateMenuProduct(menuProduct)
            findNavController().navigateUp()
        }
    }
}