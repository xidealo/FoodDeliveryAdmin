package com.bunbeauty.fooddeliveryadmin.ui.fragments.menu

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.common.Constants.PRODUCT_CODE_REQUEST_KEY
import com.bunbeauty.common.Constants.SELECTED_PRODUCT_CODE_KEY
import com.bunbeauty.domain.model.menu_product.MenuProductCode
import com.bunbeauty.domain.util.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentCreateMenuProductBinding
import com.bunbeauty.fooddeliveryadmin.extensions.launchWhenStarted
import com.bunbeauty.fooddeliveryadmin.extensions.toggleVisibility
import com.bunbeauty.fooddeliveryadmin.presentation.menu.CreateMenuProductViewModel
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class CreateMenuProductFragment : BaseFragment<FragmentCreateMenuProductBinding>() {

    @Inject
    lateinit var resourcesProvider: IResourcesProvider

    override val viewModel: CreateMenuProductViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.fragmentCreateMenuProductBtnBack.setOnClickListener {
            viewModel.goBack()
        }
        viewModel.visibilityIcon.onEach { icon ->
            viewDataBinding.fragmentCreateMenuProductBtnVisibility.icon = icon
        }.launchWhenStarted(lifecycleScope)
        viewDataBinding.fragmentCreateMenuProductBtnVisibility.setOnClickListener {
            viewModel.switchVisibility()
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
        }.launchWhenStarted(lifecycleScope)
    }
}