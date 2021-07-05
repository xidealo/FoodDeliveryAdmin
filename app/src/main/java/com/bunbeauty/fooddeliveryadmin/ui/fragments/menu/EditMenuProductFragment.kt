package com.bunbeauty.fooddeliveryadmin.ui.fragments.menu

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.bunbeauty.common.Constants.IMAGES_FOLDER
import com.bunbeauty.common.Constants.PRODUCT_CODE_REQUEST_KEY
import com.bunbeauty.common.Constants.PRODUCT_COMBO_DESCRIPTION_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_DISCOUNT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_NAME_ERROR_KEY
import com.bunbeauty.common.Constants.SELECTED_PRODUCT_CODE_KEY
import com.bunbeauty.domain.model.menu_product.MenuProductCode
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentEditMenuProductBinding
import com.bunbeauty.fooddeliveryadmin.extensions.getBitmap
import com.bunbeauty.fooddeliveryadmin.extensions.setImage
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.fooddeliveryadmin.extensions.toggleVisibility
import com.bunbeauty.fooddeliveryadmin.presentation.menu.EditMenuProductViewModel
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EditMenuProductFragment : BaseFragment<FragmentEditMenuProductBinding>() {

    override val viewModel: EditMenuProductViewModel by viewModels()

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                viewDataBinding.fragmentEditMenuProductIvPhoto.setImageURI(uri)
                viewModel.photo = viewDataBinding.fragmentEditMenuProductIvPhoto.getBitmap()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewDataBinding) {
            fragmentEditMenuProductBtnBack.setOnClickListener {
                viewModel.goBack()
            }
            fragmentEditMenuProductBtnVisibility.setOnClickListener {
                viewModel.switchVisibility()
            }
            viewModel.visibilityIcon.onEach { icon ->
                fragmentEditMenuProductBtnVisibility.icon = icon
            }.startedLaunch(viewLifecycleOwner)
            if (viewModel.photo == null) {
                fragmentEditMenuProductIvPhoto.setImage(viewModel.photoLink)
            } else {
                fragmentEditMenuProductIvPhoto.setImageBitmap(viewModel.photo)
            }
            fragmentEditMenuProductMcvPhoto.setOnClickListener {
                imageLauncher.launch(IMAGES_FOLDER)
            }
            fragmentEditMenuProductEtName.setText(viewModel.name)
            fragmentEditMenuProductNcvProductCode.cardText = viewModel.productCodeString
            setFragmentResultListener(PRODUCT_CODE_REQUEST_KEY) { _, bundle ->
                bundle.getParcelable<MenuProductCode>(SELECTED_PRODUCT_CODE_KEY)
                    ?.let { menuProductCode ->
                        fragmentEditMenuProductNcvProductCode.cardText = menuProductCode.title
                        viewModel.setProductCode(menuProductCode.title)
                    }
            }
            fragmentEditMenuProductNcvProductCode.setOnClickListener {
                viewModel.goToProductCodeList()
            }
            fragmentEditMenuProductEtCost.setText(viewModel.cost)
            fragmentEditMenuProductEtDiscountCost.setText(viewModel.discountCost)
            fragmentEditMenuProductEtWeight.setText(viewModel.weight)
            fragmentEditMenuProductEtDescription.setText(viewModel.description)
            fragmentEditMenuProductEtComboDescription.setText(viewModel.comboDescription)
            viewModel.isComboDescriptionVisible.onEach { isVisible ->
                fragmentEditMenuProductTilComboDescription.toggleVisibility(isVisible)
            }.startedLaunch(viewLifecycleOwner)
            fragmentEditMenuProductBtnDelete.setOnClickListener {
                viewModel.deleteMenuProduct()
            }
            fragmentEditMenuProductBtnSave.setOnClickListener {
                viewModel.saveMenuProduct(
                    fragmentEditMenuProductEtName.text.toString(),
                    fragmentEditMenuProductNcvProductCode.cardText,
                    fragmentEditMenuProductEtCost.text.toString(),
                    fragmentEditMenuProductEtDiscountCost.text.toString(),
                    fragmentEditMenuProductEtWeight.text.toString(),
                    fragmentEditMenuProductEtDescription.text.toString(),
                    fragmentEditMenuProductEtComboDescription.text.toString()
                )
            }

            textInputMap[PRODUCT_NAME_ERROR_KEY] = fragmentEditMenuProductTilName
            textInputMap[PRODUCT_COST_ERROR_KEY] = fragmentEditMenuProductTilCost
            textInputMap[PRODUCT_DISCOUNT_COST_ERROR_KEY] = fragmentEditMenuProductTilDiscountCost
            textInputMap[PRODUCT_COMBO_DESCRIPTION_ERROR_KEY] =
                fragmentEditMenuProductTilComboDescription
        }
    }
}