package com.bunbeauty.fooddeliveryadmin.ui.fragments.menu

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.model.ServerMenuProduct
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentCreateMenuProductBinding
import com.bunbeauty.fooddeliveryadmin.presentation.menu.CreateMenuProductViewModel
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateMenuProductFragment : BaseFragment<FragmentCreateMenuProductBinding>() {

    override val viewModel: CreateMenuProductViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProductCodesAdapter()
        viewDataBinding.fragmentCreateMenuProductBtnUpdate.setOnClickListener {
            val menuProduct = ServerMenuProduct()
            //menuProduct.uuid = CreateNewMenuProductFragmentArgs.fromBundle(requireArguments()).uuid
            menuProduct.visible = viewDataBinding.fragmentCreateMenuProductRbShow.isChecked
            menuProduct.productCode =
                viewDataBinding.fragmentCreationAddressEtProductCode.text.toString()
            menuProduct.name = viewDataBinding.fragmentCreateMenuProductEtName.text.toString()
            menuProduct.cost =
                viewDataBinding.fragmentCreateMenuProductEtCost.text.toString().toInt()
            menuProduct.weight =
                viewDataBinding.fragmentCreateMenuProductEtWeight.text.toString().toInt()
            menuProduct.description =
                viewDataBinding.fragmentCreateMenuProductEtDescription.text.toString()
            menuProduct.comboDescription =
                viewDataBinding.fragmentCreateMenuProductEtComboDescription.text.toString()
            if (viewDataBinding.fragmentCreateMenuProductEtDiscountCost.text.toString()
                    .toInt() == 0
            ) {
                menuProduct.discountCost = null
            } else {
                menuProduct.discountCost =
                    viewDataBinding.fragmentCreateMenuProductEtDiscountCost.text.toString().toInt()
            }
            viewModel.createMenuProduct(menuProduct)
            //router.navigateUp()
        }
    }

    fun setProductCodesAdapter() {
        val adapter = ArrayAdapter(
            viewDataBinding.fragmentCreationAddressEtProductCode.context,
            R.layout.support_simple_spinner_dropdown_item,
            ProductCode.values().toMutableList()
        )
        viewDataBinding.fragmentCreationAddressEtProductCode.setAdapter(adapter)
    }
}