package com.bunbeauty.fooddeliveryadmin.ui.fragments.edit_menu

import android.os.Bundle
import android.view.View
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentEditMenuProductBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.presentation.EditMenuProductViewModel

class EditMenuProductFragment :
    BaseFragment<FragmentEditMenuProductBinding, EditMenuProductViewModel>() {

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

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