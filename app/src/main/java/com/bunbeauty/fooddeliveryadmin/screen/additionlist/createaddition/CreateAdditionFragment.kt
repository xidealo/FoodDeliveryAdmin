package com.bunbeauty.fooddeliveryadmin.screen.additionlist.createaddition

import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
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
import com.bunbeauty.fooddeliveryadmin.compose.element.button.AdminButtonDefaults
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.button.SecondaryButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.SwitcherCard
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextField
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextFieldDefaults.keyboardOptions
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.toTextFieldUi
import com.bunbeauty.presentation.feature.additionlist.createaddition.CreateAddition
import com.bunbeauty.presentation.feature.additionlist.createaddition.CreateAdditionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAdditionFragment :
    BaseComposeFragment<CreateAddition.DataState, CreateAdditionViewState, CreateAddition.Action, CreateAddition.Event>() {

    override val viewModel: CreateAdditionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(CreateAddition.Action.CreateAddition)
    }

    @Composable
    override fun Screen(state: CreateAdditionViewState, onAction: (CreateAddition.Action) -> Unit) {
        CreateAdditionScreen(onAction = onAction, state = state)
    }

    @Composable
    fun CreateAdditionScreen(
        state: CreateAdditionViewState,
        onAction: (CreateAddition.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_create_addition),
            backActionClick = { onAction(CreateAddition.Action.OnBackClick) },
            actionButton = {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = spacedBy(8.dp)
                ) {
                    AddPhotoButton(
                        isError = false,
                        onClick = {}
                    )
                    LoadingButton(
                        text = stringResource( R.string.action_create_addition_save),
                        onClick = { onAction(CreateAddition.Action.OnSaveCreateAdditionClick) },
                        isLoading = state.isLoading
                    )
                }
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
                            value = state.name.value,
                            labelText = stringResource(R.string.hint_create_addition_name),
                            onValueChange = { name ->
                                onAction(
                                    CreateAddition.Action.CreateNameAddition(name)
                                )
                            },
                            isError = state.name.isError,
                            errorText = stringResource(state.name.errorResId),
                            enabled = !state.isLoading,
                            keyboardOptions = keyboardOptions(
                                keyboardType = KeyboardType.Text
                            )
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            labelText = stringResource(R.string.hint_create_addition_priority),
                            value = state.priority.value,
                            onValueChange = { priority ->
                                onAction(
                                    CreateAddition.Action.CreatePriorityAddition(priority)
                                )
                            },
                            isError = state.priority.isError,
                            errorText = stringResource(state.priority.errorResId),
                            enabled = !state.isLoading,
                            keyboardOptions = keyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.fullName.toString(),
                            labelText = stringResource(R.string.hint_create_addition_full_name),
                            onValueChange = { fullName ->
                                onAction(
                                    CreateAddition.Action.CreateFullNameAddition(fullName)
                                )
                            },
                            maxLines = 20,
                            enabled = !state.isLoading,
                            keyboardOptions = keyboardOptions(
                                keyboardType = KeyboardType.Text
                            )
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.price.value,
                            labelText = stringResource(R.string.hint_create_addition_price),
                            isError = state.price.isError,
                            errorText = stringResource(state.price.errorResId),
                            onValueChange = { price ->
                                onAction(CreateAddition.Action.CreatePriceAddition(price))
                            },
                            enabled = !state.isLoading,
                            keyboardOptions = keyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                    }
                }
                SwitcherCard(
                    modifier = Modifier.padding(top = 8.dp),
                    checked = state.isVisible,
                    onCheckChanged = { isVisible ->
                        onAction(
                            CreateAddition.Action.OnVisibleClick(
                                isVisible = isVisible
                            )
                        )
                    },
                    // text = getString(R.string.title_create_addition_is_visible),
                    text = "Показывать в меню",
                    enabled = !state.isLoading
                )
                Spacer(modifier = Modifier.height(AdminTheme.dimensions.scrollScreenBottomSpace))
            }
        }
    }

    @Composable
    private fun AddPhotoButton(
        isError: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        SecondaryButton(
            modifier = modifier,
            textStringId = R.string.title_create_addition_add_photo,
            onClick = onClick,
            isError = isError,
            borderColor = if (isError) {
                AdminTheme.colors.main.error
            } else {
                AdminTheme.colors.main.primary
            },
            buttonColors = AdminButtonDefaults.accentSecondaryButtonColors
        )
    }

    @Composable
    override fun mapState(state: CreateAddition.DataState): CreateAdditionViewState {
        return CreateAdditionViewState(
            name = state.name.toTextFieldUi(errorResId = R.string.error_create_addition_empty_name),
            priority = state.priority.toTextFieldUi(errorResId = R.string.error_create_addition_empty_price),
            fullName = state.fullName,
            price = state.price.toTextFieldUi(errorResId = R.string.error_create_addition_empty_price),
            isVisible = state.isVisible,
            isLoading = state.isLoading

        )
    }

    override fun handleEvent(event: CreateAddition.Event) {
        when (event) {
            is CreateAddition.Event.Back -> {
                findNavController().navigateUp()
            }

            is CreateAddition.Event.ShowSaveAdditionSuccess -> {
                resources.getString(R.string.msg_edit_addition_updated, event.additionName)
                findNavController().popBackStack()
            }

            is CreateAddition.Event.ShowSomethingWentWrong -> {
                (activity as? MessageHost)?.showErrorMessage(
                    resources.getString(R.string.error_common_something_went_wrong)
                )
            }
        }
    }

    @Preview
    @Composable
    fun CreateAdditionScreenPreview() {
        AdminTheme {
            CreateAdditionScreen(
                state = CreateAdditionViewState(
                    name = TextFieldUi.empty,
                    priority = TextFieldUi.empty,
                    fullName = "30",
                    price = TextFieldUi.empty,
                    isVisible = false,
                    isLoading = false

                ),

                onAction = {}
            )
        }
    }
}
