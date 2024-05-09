package com.bunbeauty.fooddeliveryadmin.screen.additionlist.editaddition

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.SwitcherCard
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextField
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.presentation.feature.additionlist.AdditionList
import com.bunbeauty.presentation.feature.additionlist.editadditionlist.EditAddition
import com.bunbeauty.presentation.feature.additionlist.editadditionlist.EditAdditionViewModel


class EditAdditionFragment :
    BaseComposeFragment<EditAddition.DataState, EditAdditionViewState, EditAddition.Action, EditAddition.Event>() {

    override val viewModel: EditAdditionViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAction(EditAddition.Action.Init)
    }

    @Composable
    override fun Screen(state: EditAdditionViewState, onAction: (EditAddition.Action) -> Unit) {
    }

    @Composable
    fun EditAdditionScreen(
        onAction: (EditAddition.Action) -> Unit,
        onNameTextChanged: (value: String) -> Unit,
        onPriorityTextChanged: (value: String) -> Unit,
        onPriseTextChanged: (value: String) -> Unit,
        onFullNameTextChanged: (value: String) -> Unit,
        state: EditAdditionViewState,
    ) {
        AdminScaffold(
            title = "Редактировать добавку",
            pullRefreshEnabled = true,
            backActionClick = { onAction(EditAddition.Action.OnBackClick) },
            actionButton = {
                LoadingButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textStringId = R.string.action_order_details_save,
                    onClick = { onAction(EditAddition.Action.SaveEditAdditionClick) },
                    isLoading = state.isLoading
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            ) {
                AdminCard(
                    clickable = false
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
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
                            enabled = !state.isLoading,
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.priority,
                            labelStringId = R.string.hint_edit_menu_product_priority,
                            onValueChange = onPriorityTextChanged,
                            errorMessageId = null,
                            enabled = !state.isLoading,
                            keyboardType = KeyboardType.Number
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.fullName,
                            labelStringId = R.string.hint_edit_menu_product_full_name,
                            errorMessageId = if (state.hasFullNameError) {
                                R.string.error_edit_menu_product_empty_name
                            } else {
                                null
                            },
                            onValueChange = onFullNameTextChanged,
                            maxLines = 20,
                            enabled = !state.isLoading,
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.price,
                            labelStringId = R.string.hint_edit_menu_product_new_price,
                            errorMessageId = null,
                            onValueChange = onPriseTextChanged,
                            enabled = !state.isLoading,
                            keyboardType = KeyboardType.Number
                        )
                    }
                }
                SwitcherCard(
                    modifier = Modifier.padding(top = 8.dp),
                    checked = true,
                    onCheckChanged = {
                        onAction(
                            EditAddition.Action.OnVisibleClick(
                                isVisible = state.isVisible,
                            )
                        )
                    },
                    labelStringId = R.string.title_edit_menu_product_is_visible,
                    enabled = !state.isLoading
                )
                Spacer(modifier = Modifier.height(AdminTheme.dimensions.scrollScreenBottomSpace))
            }
        }
    }


    @Composable
    override fun mapState(dataState: EditAddition.DataState): EditAdditionViewState {
        return EditAdditionViewState(
            name = dataState.name,
            priority = dataState.priority.toString(),
            fullName = dataState.fullName ?: "",
            price = dataState.prise.toString(),
            isVisible = dataState.isVisible,
            isLoading = dataState.isLoading,
            error = dataState.error,
            hasNameError = dataState.hasNameError,
            hasFullNameError = dataState.hasFullNameError,
        )
    }

    override fun handleEvent(event: EditAddition.Event) {
        when (event) {
            is EditAddition.Event.Back -> {
                findNavController().navigateUp()
            }
        }
    }


    @Preview(showSystemUi = true)
    @Composable
    fun EditAdditionScreenPreview() {
        AdminTheme {
            EditAdditionScreen(
                state = EditAdditionViewState(
                    name = "бекон",
                    priority = "1",
                    fullName = "бекон свинной",
                    price = "50",
                    isVisible = true,
                    isLoading = false,
                    error = null,
                    hasNameError = false,
                    hasFullNameError = false,
                ),
                onAction = {},
                onNameTextChanged = {},
                onPriorityTextChanged = {},
                onPriseTextChanged = {},
                onFullNameTextChanged = {},
            )
        }
    }
}