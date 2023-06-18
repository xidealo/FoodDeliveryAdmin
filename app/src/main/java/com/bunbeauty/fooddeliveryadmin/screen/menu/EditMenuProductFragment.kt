package com.bunbeauty.fooddeliveryadmin.screen.menu

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.MainButton
import com.bunbeauty.fooddeliveryadmin.compose.element.text_field.AdminTextField
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentComposeBinding
import com.bunbeauty.fooddeliveryadmin.util.compose
import com.bunbeauty.presentation.utils.IResourcesProvider
import com.bunbeauty.presentation.view_model.menu.edit_menu_product.EditMenuProductEvent
import com.bunbeauty.presentation.view_model.menu.edit_menu_product.EditMenuProductUIState
import com.bunbeauty.presentation.view_model.menu.edit_menu_product.EditMenuProductViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditMenuProductFragment : BaseFragment<FragmentComposeBinding>() {

    @Inject
    lateinit var resourcesProvider: IResourcesProvider

    override val viewModel: EditMenuProductViewModel by viewModels()

    private val editMenuProductFragmentArgs: EditMenuProductFragmentArgs by navArgs()

//    private val imageLauncher =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            if (uri != null) {
//                binding.fragmentEditMenuProductIvPhoto.setImageURI(uri)
//                viewModel.photo = binding.fragmentEditMenuProductIvPhoto.getBitmap()
//            }
//        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadData(editMenuProductFragmentArgs.menuProductUuid)

        binding.root.compose {
            val editMenuProductUIState by viewModel.editMenuProductUiState.collectAsStateWithLifecycle()
            EditMenuProductScreen(editMenuProductUIState = editMenuProductUIState)
        }
    }

    @Composable
    fun EditMenuProductScreen(editMenuProductUIState: EditMenuProductUIState) {
        AdminScaffold(
            title = editMenuProductUIState.title,
            backActionClick = findNavController()::popBackStack
        ) {
            when (val state = editMenuProductUIState.editMenuProductState) {
                EditMenuProductUIState.EditMenuProductState.Error -> EditMenuProductErrorScreen()
                EditMenuProductUIState.EditMenuProductState.Loading -> EditMenuProductLoadingScreen()
                is EditMenuProductUIState.EditMenuProductState.Success -> EditMenuProductSuccessScreen(
                    state
                )
            }
            LaunchedEffect(editMenuProductUIState.eventList) {
                handleEventList(editMenuProductUIState.eventList)
            }
        }
    }

    private fun handleEventList(eventList: List<EditMenuProductEvent>) {
        eventList.forEach { event ->
            when (event) {
                is EditMenuProductEvent.MoveBack -> {
                }
            }
        }
        viewModel.consumeEvents(eventList)
    }

    @Composable
    fun EditMenuProductSuccessScreen(state: EditMenuProductUIState.EditMenuProductState.Success) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp
            )
        ) {
            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.name,
                labelStringId = R.string.hint_edit_menu_product_name,
                onValueChange = { value ->
                    viewModel.onNameTextChanged(value)
                },
                errorMessageId = if (state.hasNameError) {
                    R.string.error_edit_menu_product_empty_name
                } else {
                    null
                },
            )

            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.description,
                labelStringId = R.string.hint_edit_menu_product_description,
                onValueChange = { value ->
                    viewModel.onDescriptionTextChanged(value)
                },
                maxLines = 5,
                errorMessageId = if (state.hasDescriptionError) {
                    R.string.error_edit_menu_product_empty_description
                } else {
                    null
                },
            )

            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.newPrice,
                labelStringId = R.string.hint_edit_menu_product_new_price,
                onValueChange = { value ->
                    viewModel.onNewPriceTextChanged(value)
                },
                errorMessageId = if (state.hasNewPriceError) {
                    R.string.error_edit_menu_product_empty_new_price
                } else {
                    null
                },
            )

            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.oldPrice,
                labelStringId = R.string.hint_edit_menu_product_old_price,
                onValueChange = { value ->
                    viewModel.onOldPriceTextChanged(value)
                },
                errorMessageId = if (state.hasOldPriceError) {
                    R.string.error_edit_menu_product_empty_name
                } else {
                    null
                },
            )

            Spacer(modifier = Modifier.weight(1f))

            MainButton(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(bottom = 16.dp),
                textStringId = R.string.action_order_details_save,
                onClick = viewModel::updateMenuProduct
            )
        }
    }

    @Composable
    fun EditMenuProductErrorScreen() {
        ErrorScreen(
            mainTextId = R.string.title_common_can_not_load_data,
            extraTextId = R.string.msg_common_check_connection_and_retry,
            onClick = { /*viewModel::loadData*/ }
        )
    }

    @Composable
    fun EditMenuProductLoadingScreen() {
        LoadingScreen()
    }

    @Preview(showSystemUi = true)
    @Composable
    fun EditMenuProductSuccessScreenPreview() {
        AdminTheme {
            EditMenuProductSuccessScreen(
                EditMenuProductUIState.EditMenuProductState.Success(
                    name = "Продукт",
                    hasNameError = false,
                    description = "Описание",
                    hasDescriptionError = false,
                    newPrice = "Актуальная цена",
                    hasNewPriceError = false,
                    oldPrice = "Цена до скидки",
                    hasOldPriceError = false,
                )
            )
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun EditMenuProductErrorScreenPreview() {
        AdminTheme {
            EditMenuProductErrorScreen()
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun EditMenuProductLoadingScreenPreview() {
        AdminTheme {
            EditMenuProductLoadingScreen()
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
