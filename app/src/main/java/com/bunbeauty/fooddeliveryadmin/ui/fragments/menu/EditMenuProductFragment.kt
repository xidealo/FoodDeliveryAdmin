package com.bunbeauty.fooddeliveryadmin.ui.fragments.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentEditMenuProductBinding
import com.bunbeauty.fooddeliveryadmin.presentation.menu.EditMenuProductViewModel
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditMenuProductFragment :
    BaseFragment<FragmentEditMenuProductBinding, EditMenuProductViewModel>() {

    override val viewModel: EditMenuProductViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val menuProduct = EditMenuProductFragmentArgs.fromBundle(requireArguments()).menuProduct
        //viewDataBinding.menuProduct = menuProduct

        viewDataBinding.fragmentEditMenuProductBtnUpdate.setOnClickListener {
            /*menuProduct.visible = viewDataBinding.fragmentEditMenuProductRbShow.isChecked
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
            router.navigateUp()*/
        }
    }
}