package com.bunbeauty.fooddeliveryadmin.screen.additionlist.editaddition

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.presentation.feature.additionlist.editadditionlist.EditAddition
import com.bunbeauty.presentation.feature.additionlist.editadditionlist.EditAdditionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditAdditionFragment :
    BaseComposeFragment<EditAddition.DataState, EditAdditionViewState, EditAddition.Action, EditAddition.Event>() {

    override val viewModel: EditAdditionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(EditAddition.Action.InitAddition)
    }

    @Composable
    override fun Screen(state: EditAdditionViewState, onAction: (EditAddition.Action) -> Unit) {
        EditAdditionScreen(onAction = onAction, state = state)
    }

    @Composable
    fun EditAdditionScreen(
        state: EditAdditionViewState,
        onAction: (EditAddition.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_edit_addition),
            backActionClick = { onAction(EditAddition.Action.OnBackClick) },
            actionButton = {
                LoadingButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textStringId = R.string.action_edit_addition_save,
                    onClick = { onAction(EditAddition.Action.OnSaveEditAdditionClick) },
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
                            labelStringId = R.string.hint_edit_addition_name,
                            onValueChange = { name ->
                                onAction(
                                    EditAddition.Action.EditNameAddition(name)
                                )
                            },
                            errorMessageId = state.editNameError,
                            enabled = !state.isLoading,
                            keyboardType = KeyboardType.Text
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.priority,
                            labelStringId = R.string.hint_edit_addition_priority,
                            onValueChange = { priority ->
                                onAction(
                                    EditAddition.Action.EditPriorityAddition(priority)
                                )
                            },
                            enabled = !state.isLoading,
                            keyboardType = KeyboardType.Number
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.fullName,
                            labelStringId = R.string.hint_edit_addition_full_name,
                            onValueChange = { fullName ->
                                onAction(
                                    EditAddition.Action.EditFullNameAddition(fullName)
                                )
                            },
                            maxLines = 20,
                            enabled = !state.isLoading,
                            keyboardType = KeyboardType.Text
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.price,
                            labelStringId = R.string.hint_edit_addition_new_price,
                            errorMessageId = state.editPriceError,
                            onValueChange = { price ->
                                onAction(EditAddition.Action.EditPriceAddition(price))
                            },
                            enabled = !state.isLoading,
                            keyboardType = KeyboardType.Number
                        )
                    }
                }
                SwitcherCard(
                    modifier = Modifier.padding(top = 8.dp),
                    checked = state.isVisible,
                    onCheckChanged = { isVisible ->
                        onAction(
                            EditAddition.Action.OnVisibleClick(
                                isVisible = isVisible
                            )
                        )
                    },
                    text = getString(R.string.title_edit_addition_is_visible),
                    enabled = !state.isLoading
                )
                Spacer(modifier = Modifier.height(AdminTheme.dimensions.scrollScreenBottomSpace))
            }
        }
    }

    @Composable
    override fun mapState(state: EditAddition.DataState): EditAdditionViewState {
        return EditAdditionViewState(
            name = state.name,
            editNameError = if (state.hasEditNameError) {
                R.string.error_edit_addition_empty_name
            } else {
                null
            },
            priority = state.priority.toString(),
            fullName = state.fullName ?: "",
            price = state.price?.toString() ?: "",
            editPriceError = if (state.hasEditPriceError) {
                R.string.error_add_addition_empty_new_price
            } else {
                null
            },
            isVisible = state.isVisible,
            isLoading = state.isLoading
        )
    }

    override fun handleEvent(event: EditAddition.Event) {
        when (event) {
            is EditAddition.Event.Back -> {
                findNavController().navigateUp()
            }

            is EditAddition.Event.ShowUpdateAdditionSuccess -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(R.string.msg_edit_addition_updated, event.additionName)
                )
                findNavController().popBackStack()
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun EditAdditionScreenPreview() {
        AdminTheme {
            EditAdditionScreen(
                state = EditAdditionViewState(
                    name = "",
                    priority = "",
                    fullName = "",
                    price = "",
                    isVisible = false,
                    isLoading = false,
                    editNameError = null,
                    editPriceError = null
                ),
                onAction = {}
            )
        }
    }
}
