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
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
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
                        onClick = {

                        }
                    )
                    LoadingButton(
                        //modifier = Modifier.padding(horizontal = 16.dp),
                        textStringId = R.string.action_edit_addition_save,
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
                            value = state.name,
                            labelStringId = R.string.hint_edit_addition_name,
                            onValueChange = { name ->
                                onAction(
                                    CreateAddition.Action.CreateNameAddition(name)
                                )
                            },
                            errorMessageId = state.createNameError,
                            enabled = !state.isLoading,
                            keyboardType = KeyboardType.Text
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.priority,
                            labelStringId = R.string.hint_edit_addition_priority,
                            onValueChange = { priority ->
                                onAction(
                                    CreateAddition.Action.CreatePriorityAddition(priority)
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
                                    CreateAddition.Action.CreateFullNameAddition(fullName)
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
                            errorMessageId = state.createPriceError,
                            onValueChange = { price ->
                                onAction(CreateAddition.Action.CreatePriceAddition(price))
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
                            CreateAddition.Action.OnVisibleClick(
                                isVisible = isVisible
                            )
                        )
                    },
                    //text = getString(R.string.title_edit_addition_is_visible),
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
            textStringId = R.string.title_add_menu_product_add_photo,
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
            name = state.name,
            createNameError = if (state.hasCreateNameError) {
                R.string.error_edit_addition_empty_name
            } else {
                null
            },
            priority = state.priority.toString(),
            fullName = state.fullName ?: "",
            price = state.price.toString(),
            createPriceError = if (state.hasCreatePriceError) {
                R.string.error_add_addition_empty_new_price
            } else {
                null
            },
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

        }
    }

    @Preview
    @Composable
    fun CreateAdditionScreenPreview() {
        AdminTheme {
            CreateAdditionScreen(
                state = CreateAdditionViewState(
                    name = "Гриб",
                    priority = "1",
                    fullName = "Гриб белый",
                    price = "30",
                    isVisible = false,
                    isLoading = false,
                    createNameError = null,
                    createPriceError = null
                ),

                onAction = {}
            )
        }
    }
}