package com.bunbeauty.fooddeliveryadmin.ui.fragments.menu

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.common.Constants.IMAGES_FOLDER
import com.bunbeauty.common.Constants.PRODUCT_CODE_REQUEST_KEY
import com.bunbeauty.common.Constants.PRODUCT_COMBO_DESCRIPTION_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_DISCOUNT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_NAME_ERROR_KEY
import com.bunbeauty.common.Constants.SELECTED_PRODUCT_CODE_KEY
import com.bunbeauty.domain.model.menu_product.MenuProductCode
import com.bunbeauty.domain.util.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentCreateMenuProductBinding
import com.bunbeauty.fooddeliveryadmin.extensions.getBitmap
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.fooddeliveryadmin.extensions.toggleVisibility
import com.bunbeauty.fooddeliveryadmin.presentation.menu.CreateMenuProductViewModel
import com.bunbeauty.fooddeliveryadmin.ui.ErrorEvent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class CreateMenuProductFragment : BaseFragment<FragmentCreateMenuProductBinding>() {

    @Inject
    lateinit var resourcesProvider: IResourcesProvider

    override val viewModel: CreateMenuProductViewModel by viewModels()

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                viewDataBinding.fragmentCreateMenuProductIvPhoto.setImageURI(uri)
                viewModel.image = viewDataBinding.fragmentCreateMenuProductIvPhoto.getBitmap()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.fragmentCreateMenuProductBtnBack.setOnClickListener {
            viewModel.goBack()
        }
        viewModel.visibilityIcon.onEach { icon ->
            viewDataBinding.fragmentCreateMenuProductBtnVisibility.icon = icon
        }.startedLaunch(lifecycle)
        viewDataBinding.fragmentCreateMenuProductBtnVisibility.setOnClickListener {
            viewModel.switchVisibility()
        }
        if (viewModel.image != null) {
            viewDataBinding.fragmentCreateMenuProductIvPhoto.setImageBitmap(viewModel.image)
        }
        viewDataBinding.fragmentCreateMenuProductMcvPhoto.setOnClickListener {
            imageLauncher.launch(IMAGES_FOLDER)
        }
        setFragmentResultListener(PRODUCT_CODE_REQUEST_KEY) { _, bundle ->
            bundle.getParcelable<MenuProductCode>(SELECTED_PRODUCT_CODE_KEY)
                ?.let { menuProductCode ->
                    viewDataBinding.fragmentCreateMenuProductNcvProductCode.cardText =
                        menuProductCode.title
                    viewModel.setProductCode(menuProductCode.title)
                }
        }
        viewDataBinding.fragmentCreateMenuProductNcvProductCode.setOnClickListener {
            viewModel.goToProductCodeList()
        }
        viewModel.isComboDescriptionVisible.onEach { isVisible ->
            viewDataBinding.fragmentCreateMenuProductTilComboDescription.toggleVisibility(isVisible)
        }.startedLaunch(lifecycle)
        viewModel.error.onEach { error ->
            if (error != null) {
                viewDataBinding.fragmentCreateMenuProductTilName.error = null
                viewDataBinding.fragmentCreateMenuProductTilCost.error = null
                viewDataBinding.fragmentCreateMenuProductTilDiscountCost.error = null
                viewDataBinding.fragmentCreateMenuProductTilComboDescription.error = null
                when (error) {
                    is ErrorEvent.MessageError -> {
                        showError(error.message)
                    }
                    is ErrorEvent.FieldError -> {
                        val textInputLayout = when (error.key) {
                            PRODUCT_NAME_ERROR_KEY ->
                                viewDataBinding.fragmentCreateMenuProductTilName
                            PRODUCT_COST_ERROR_KEY ->
                                viewDataBinding.fragmentCreateMenuProductTilCost
                            PRODUCT_DISCOUNT_COST_ERROR_KEY ->
                                viewDataBinding.fragmentCreateMenuProductTilDiscountCost
                            PRODUCT_COMBO_DESCRIPTION_ERROR_KEY ->
                                viewDataBinding.fragmentCreateMenuProductTilComboDescription
                            else -> null
                        }
                        textInputLayout?.error = error.message
                        textInputLayout?.requestFocus()
                    }
                }
            }
        }.launchIn(lifecycleScope)
        viewModel.message.onEach { message ->
            if (message != null) {
                showMessage(message)
            }
        }.launchIn(lifecycleScope)
        viewDataBinding.fragmentCreateMenuProductBtnCreate.setOnClickListener {
            viewModel.createMenuProduct(
                viewDataBinding.fragmentCreateMenuProductIvPhoto.getBitmap(),
                viewDataBinding.fragmentCreateMenuProductEtName.text.toString(),
                viewDataBinding.fragmentCreateMenuProductNcvProductCode.cardText,
                viewDataBinding.fragmentCreateMenuProductEtCost.text.toString(),
                viewDataBinding.fragmentCreateMenuProductEtDiscountCost.text.toString(),
                viewDataBinding.fragmentCreateMenuProductEtWeight.text.toString(),
                viewDataBinding.fragmentCreateMenuProductEtDescription.text.toString(),
                viewDataBinding.fragmentCreateMenuProductEtComboDescription.text.toString()
            )
        }
    }
}