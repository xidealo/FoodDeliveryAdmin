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
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentCreateMenuProductBinding
import com.bunbeauty.fooddeliveryadmin.extensions.getBitmap
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.fooddeliveryadmin.extensions.toggleVisibility
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.menu.CreateMenuProductFragmentDirections.toListBottomSheet
import com.bunbeauty.presentation.model.list.MenuProductCode
import com.bunbeauty.presentation.navigation_event.CreateMenuProductNavigationEvent
import com.bunbeauty.presentation.utils.IResourcesProvider
import com.bunbeauty.presentation.view_model.menu.CreateMenuProductViewModel
import dagger.hilt.android.AndroidEntryPoint
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
                binding.fragmentCreateMenuProductIvPhoto.setImageURI(uri)
                viewModel.photo = binding.fragmentCreateMenuProductIvPhoto.getBitmap()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            fragmentCreateMenuProductBtnBack.setOnClickListener {
                viewModel.goBack()
            }
            fragmentCreateMenuProductBtnVisibility.setOnClickListener {
                viewModel.switchVisibility()
            }
            viewModel.isVisible.onEach { isVisible ->
                fragmentCreateMenuProductBtnVisibility.icon = if (isVisible) {
                    resourcesProvider.getDrawable(R.drawable.ic_visible)
                } else {
                    resourcesProvider.getDrawable(R.drawable.ic_invisible)
                }
            }.startedLaunch(viewLifecycleOwner)
            if (viewModel.photo != null) {
                fragmentCreateMenuProductIvPhoto.setImageBitmap(viewModel.photo)
            }
            fragmentCreateMenuProductMcvPhoto.setOnClickListener {
                imageLauncher.launch(IMAGES_FOLDER)
            }
            setFragmentResultListener(PRODUCT_CODE_REQUEST_KEY) { _, bundle ->
                bundle.getParcelable<MenuProductCode>(SELECTED_PRODUCT_CODE_KEY)
                    ?.let { menuProductCode ->
                        fragmentCreateMenuProductNcvProductCode.cardText = menuProductCode.title
                        viewModel.setProductCode(menuProductCode)
                    }
            }
            fragmentCreateMenuProductNcvProductCode.setOnClickListener {
                viewModel.goToProductCodeList()
            }
            viewModel.isComboDescriptionVisible.onEach { isVisible ->
                fragmentCreateMenuProductTilComboDescription.toggleVisibility(isVisible)
            }.startedLaunch(viewLifecycleOwner)

            textInputMap[PRODUCT_NAME_ERROR_KEY] = fragmentCreateMenuProductTilName
            textInputMap[PRODUCT_COST_ERROR_KEY] = fragmentCreateMenuProductTilCost
            textInputMap[PRODUCT_DISCOUNT_COST_ERROR_KEY] = fragmentCreateMenuProductTilDiscountCost
            textInputMap[PRODUCT_COMBO_DESCRIPTION_ERROR_KEY] =
                fragmentCreateMenuProductTilComboDescription

            fragmentCreateMenuProductBtnCreate.setOnClickListener {
                viewModel.createMenuProduct(
                    fragmentCreateMenuProductEtName.text.toString(),
                    fragmentCreateMenuProductNcvProductCode.cardText,
                    fragmentCreateMenuProductEtCost.text.toString(),
                    fragmentCreateMenuProductEtDiscountCost.text.toString(),
                    fragmentCreateMenuProductEtWeight.text.toString(),
                    fragmentCreateMenuProductEtDescription.text.toString(),
                    fragmentCreateMenuProductEtComboDescription.text.toString()
                )
            }
        }

        viewModel.navigation.onEach { navigationEvent ->
            when (navigationEvent) {
                is CreateMenuProductNavigationEvent.ToProductCodeList ->
                    router.navigate(toListBottomSheet(navigationEvent.listData))
                else -> Unit
            }
        }.startedLaunch(viewLifecycleOwner)
    }
}