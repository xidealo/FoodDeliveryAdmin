package com.bunbeauty.fooddeliveryadmin.screen.menu

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.domain.model.Suggestion
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.SwitcherCard
import com.bunbeauty.fooddeliveryadmin.compose.element.text_field.AdminTextField
import com.bunbeauty.fooddeliveryadmin.compose.element.text_field.AdminTextFieldWithMenu
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.setContentWithTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.LayoutComposeBinding
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.presentation.utils.IResourcesProvider
import com.bunbeauty.presentation.view_model.menu.edit_menu_product.EditMenuProductEvent
import com.bunbeauty.presentation.view_model.menu.edit_menu_product.EditMenuProductUIState
import com.bunbeauty.presentation.view_model.menu.edit_menu_product.EditMenuProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditMenuProductFragment : BaseFragment<LayoutComposeBinding>() {

    @Inject
    lateinit var resourcesProvider: IResourcesProvider

    override val viewModel: EditMenuProductViewModel by viewModels()

    private val editMenuProductFragmentArgs: EditMenuProductFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadData(editMenuProductFragmentArgs.menuProductUuid)

        binding.root.setContentWithTheme {
            val editMenuProductUIState by viewModel.state.collectAsStateWithLifecycle()
            EditMenuProductScreen(
                editMenuProductUIState = editMenuProductUIState,
                onNameTextChanged = viewModel::onNameTextChanged,
                onNewPriceTextChanged = viewModel::onNewPriceTextChanged,
                onOldPriceTextChanged = viewModel::onOldPriceTextChanged,
                onNutritionTextChanged = viewModel::onNutritionTextChanged,
                onSuggestedUtilsSelected = viewModel::onSuggestedUtilsSelected,
                onDescriptionTextChanged = viewModel::onDescriptionTextChanged,
                onVisibleChanged = viewModel::onVisibleChanged,
                onErrorClick = {
                    viewModel.loadData(editMenuProductFragmentArgs.menuProductUuid)
                },
                updateMenuProductClick = viewModel::updateMenuProduct,
                backAction = findNavController()::popBackStack
            )
            LaunchedEffect(editMenuProductUIState.eventList) {
                handleEventList(editMenuProductUIState.eventList)
            }
        }
    }

    @Composable
    fun EditMenuProductScreen(
        editMenuProductUIState: EditMenuProductUIState,
        onNameTextChanged: (value: String) -> Unit,
        onNewPriceTextChanged: (value: String) -> Unit,
        onOldPriceTextChanged: (value: String) -> Unit,
        onNutritionTextChanged: (value: String) -> Unit,
        onSuggestedUtilsSelected: (value: Suggestion) -> Unit,
        onDescriptionTextChanged: (value: String) -> Unit,
        onVisibleChanged: (value: Boolean) -> Unit,
        onErrorClick: () -> Unit,
        updateMenuProductClick: () -> Unit,
        backAction: () -> Unit,
    ) {
        AdminScaffold(
            title = editMenuProductUIState.title,
            backActionClick = backAction,
            actionButton = {
                LoadingButton(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(bottom = 16.dp),
                    textStringId = R.string.action_order_details_save,
                    onClick = updateMenuProductClick,
                    isLoading = editMenuProductUIState.getSuccessState?.isLoadingButton == true
                )
            }
        ) {
            when (val state = editMenuProductUIState.editMenuProductState) {
                EditMenuProductUIState.EditMenuProductState.Error -> EditMenuProductErrorScreen(
                    onClick = onErrorClick
                )

                EditMenuProductUIState.EditMenuProductState.Loading -> LoadingScreen()
                is EditMenuProductUIState.EditMenuProductState.Success -> EditMenuProductSuccessScreen(
                    state = state,
                    onNameTextChanged = onNameTextChanged,
                    onNewPriceTextChanged = onNewPriceTextChanged,
                    onOldPriceTextChanged = onOldPriceTextChanged,
                    onNutritionTextChanged = onNutritionTextChanged,
                    onSuggestedUtilsSelected = onSuggestedUtilsSelected,
                    onDescriptionTextChanged = onDescriptionTextChanged,
                    onVisibleChanged = onVisibleChanged
                )
            }
        }
    }

    private fun handleEventList(eventList: List<EditMenuProductEvent>) {
        eventList.forEach { event ->
            when (event) {
                is EditMenuProductEvent.ShowUpdatProductSuccess -> {
                    (activity as? MessageHost)?.showInfoMessage(
                        resources.getString(R.string.msg_product_updated, event.productName)
                    )
                    findNavController().popBackStack()
                }

                is EditMenuProductEvent.ShowUpdateProductError -> {
                    (activity as? MessageHost)?.showErrorMessage(
                        resources.getString(R.string.error_product_updated)
                    )
                }
            }
        }
        viewModel.consumeEvents(eventList)
    }

    @Composable
    fun EditMenuProductSuccessScreen(
        state: EditMenuProductUIState.EditMenuProductState.Success,
        onNameTextChanged: (value: String) -> Unit,
        onNewPriceTextChanged: (value: String) -> Unit,
        onOldPriceTextChanged: (value: String) -> Unit,
        onNutritionTextChanged: (value: String) -> Unit,
        onSuggestedUtilsSelected: (value: Suggestion) -> Unit,
        onDescriptionTextChanged: (value: String) -> Unit,
        onVisibleChanged: (value: Boolean) -> Unit,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            AdminCard(
                clickable = false
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    AdminTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.name,
                        labelStringId = R.string.hint_edit_menu_product_name,
                        onValueChange = onNameTextChanged,
                        errorMessageId = if (state.hasNameError) {
                            R.string.error_edit_menu_product_empty_name
                        } else {
                            null
                        },
                        enabled = !state.isLoadingButton
                    )

                    AdminTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.newPrice,
                        labelStringId = R.string.hint_edit_menu_product_new_price,
                        onValueChange = onNewPriceTextChanged,
                        errorMessageId = if (state.hasNewPriceError) {
                            R.string.error_edit_menu_product_empty_new_price
                        } else {
                            null
                        },
                        enabled = !state.isLoadingButton,
                        keyboardType = KeyboardType.Number
                    )

                    AdminTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.oldPrice,
                        labelStringId = R.string.hint_edit_menu_product_old_price,
                        onValueChange = onOldPriceTextChanged,
                        errorMessageId = if (state.hasOldPriceError) {
                            R.string.error_edit_menu_product_empty_name
                        } else {
                            null
                        },
                        enabled = !state.isLoadingButton,
                        keyboardType = KeyboardType.Number
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        AdminTextField(
                            modifier = Modifier.weight(0.7f),
                            value = state.nutrition,
                            labelStringId = R.string.hint_edit_menu_product_nutrition,
                            onValueChange = onNutritionTextChanged,
                            errorMessageId = if (state.hasNutritionError) {
                                R.string.error_edit_menu_product_empty_nutrition
                            } else {
                                null
                            },
                            enabled = !state.isLoadingButton,
                            keyboardType = KeyboardType.Number
                        )

                        val focusManager = LocalFocusManager.current

                        var expanded by remember() {
                            mutableStateOf(false)
                        }

                        AdminTextFieldWithMenu(
                            modifier = Modifier
                                .weight(0.3f)
                                .padding(start = 8.dp)
                                .onFocusChanged { focusState ->
                                    expanded =
                                        focusState.isFocused
                                },
                            expanded = expanded,
                            onExpandedChange = { value ->
                                expanded = value
                            },
                            value = state.utils,
                            labelStringId = R.string.hint_edit_menu_product_utils,
                            onValueChange = { value ->
                                //stub don't need
                            },
                            suggestionsList = stringArrayResource(id = R.array.utilsList)
                                .mapIndexed { index, util ->
                                    Suggestion(index.toString(), util)
                                },
                            onSuggestionClick = { suggestion ->
                                focusManager.moveFocus(FocusDirection.Down)
                                onSuggestedUtilsSelected(suggestion)
                            },
                            enabled = !state.isLoadingButton
                        )
                    }

                    AdminTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.description,
                        labelStringId = R.string.hint_edit_menu_product_description,
                        onValueChange = onDescriptionTextChanged,
                        maxLines = 10,
                        errorMessageId = if (state.hasDescriptionError) {
                            R.string.error_edit_menu_product_empty_description
                        } else {
                            null
                        },
                        enabled = !state.isLoadingButton
                    )
                }
            }

            SwitcherCard(
                modifier = Modifier.padding(top = 8.dp),
                checked = state.isVisible,
                onCheckChanged = onVisibleChanged,
                labelStringId = R.string.title_edit_menu_product_is_visible,
                enabled = !state.isLoadingButton
            )
        }
    }

    @Composable
    fun EditMenuProductErrorScreen(
        onClick: () -> Unit,
    ) {
        ErrorScreen(
            mainTextId = R.string.title_common_can_not_load_data,
            extraTextId = R.string.msg_common_check_connection_and_retry,
            onClick = onClick
        )
    }

    @Preview(showSystemUi = true)
    @Composable
    fun EditMenuProductSuccessScreenPreview() {
        AdminTheme {
            EditMenuProductSuccessScreen(
                state = EditMenuProductUIState.EditMenuProductState.Success(
                    name = "Продукт",
                    hasNameError = false,
                    description = "Описание",
                    hasDescriptionError = false,
                    newPrice = "Актуальная цена",
                    hasNewPriceError = false,
                    oldPrice = "Цена до скидки",
                    hasOldPriceError = false,
                    nutrition = "200",
                    hasNutritionError = false,
                    utils = "г",
                    isLoadingButton = false,
                    isVisible = true
                ),
                onNameTextChanged = {},
                onNewPriceTextChanged = {},
                onOldPriceTextChanged = {},
                onNutritionTextChanged = {},
                onSuggestedUtilsSelected = {},
                onDescriptionTextChanged = {},
                onVisibleChanged = {},
            )
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun EditMenuProductErrorScreenPreview() {
        AdminTheme {
            EditMenuProductErrorScreen(onClick = {})
        }
    }
}
