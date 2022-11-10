package com.bunbeauty.fooddeliveryadmin.screen.menu

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import coil.load
import com.bunbeauty.common.Constants.IMAGES_FOLDER
import com.bunbeauty.common.Constants.PRODUCT_COMBO_DESCRIPTION_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_DISCOUNT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_NAME_ERROR_KEY
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentEditMenuProductBinding
import com.bunbeauty.fooddeliveryadmin.util.getBitmap
import com.bunbeauty.fooddeliveryadmin.util.startedLaunch
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.presentation.utils.IResourcesProvider
import com.bunbeauty.presentation.view_model.menu.EditMenuProductViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class EditMenuProductFragment : BaseFragment<FragmentEditMenuProductBinding>() {

    @Inject
    lateinit var resourcesProvider: IResourcesProvider

    override val viewModel: EditMenuProductViewModel by viewModels()

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.fragmentEditMenuProductIvPhoto.setImageURI(uri)
                viewModel.photo = binding.fragmentEditMenuProductIvPhoto.getBitmap()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            fragmentEditMenuProductBtnBack.setOnClickListener {

            }
            fragmentEditMenuProductBtnVisibility.setOnClickListener {
                viewModel.switchVisibility()
            }
            viewModel.isVisible.onEach { isVisible ->
                fragmentEditMenuProductBtnVisibility.icon = if (isVisible) {
                    resourcesProvider.getDrawable(R.drawable.ic_visible)
                } else {
                    resourcesProvider.getDrawable(R.drawable.ic_invisible)
                }
            }.startedLaunch(viewLifecycleOwner)
            if (viewModel.photo == null) {
                fragmentEditMenuProductIvPhoto.load(viewModel.photoLink)
            } else {
                fragmentEditMenuProductIvPhoto.setImageBitmap(viewModel.photo)
            }
            fragmentEditMenuProductMcvPhoto.setOnClickListener {
                imageLauncher.launch(IMAGES_FOLDER)
            }
            fragmentEditMenuProductEtName.setText(viewModel.name)

            fragmentEditMenuProductEtCost.setText(viewModel.cost)
            fragmentEditMenuProductEtDiscountCost.setText(viewModel.discountCost)
            fragmentEditMenuProductEtWeight.setText(viewModel.weight)
            fragmentEditMenuProductEtDescription.setText(viewModel.description)
            fragmentEditMenuProductEtComboDescription.setText(viewModel.comboDescription)
            viewModel.isComboDescriptionVisible.onEach { isVisible ->
                fragmentEditMenuProductTilComboDescription.isVisible = isVisible
            }.startedLaunch(viewLifecycleOwner)
            fragmentEditMenuProductBtnDelete.setOnClickListener {
                showDeleteDialog()
            }
            fragmentEditMenuProductBtnSave.setOnClickListener {
                viewModel.saveMenuProduct(
                    fragmentEditMenuProductEtName.text.toString(),
                    "",
                    //fragmentEditMenuProductNcvProductCode.cardText,
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

    fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.title_edit_menu_product_alert)
            .setMessage(R.string.msg_edit_menu_product_alert)
            .setPositiveButton(R.string.action_edit_menu_product_alert_yes) { _, _ ->
                viewModel.deleteMenuProduct()
            }
            .setNegativeButton(R.string.action_edit_menu_product_alert_cancel) { _, _ -> }
            .show()
    }
}