package com.bunbeauty.fooddeliveryadmin.screen.menulist.addmenuproduct

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextField
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.presentation.feature.menulist.addmenuproduct.AddMenuProduct
import com.bunbeauty.presentation.feature.menulist.addmenuproduct.AddMenuProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMenuProductFragment :
    BaseComposeFragment<AddMenuProduct.ViewDataState, AddMenuProductViewState, AddMenuProduct.Action, AddMenuProduct.Event>() {

    override val viewModel: AddMenuProductViewModel by viewModels()

    @Composable
    override fun Screen(state: AddMenuProductViewState, onAction: (AddMenuProduct.Action) -> Unit) {
        AddMenuProductScreen(state = state, onAction = onAction)
    }

    @Composable
    fun AddMenuProductScreen(
        state: AddMenuProductViewState,
        onAction: (AddMenuProduct.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_add_menu_product),
            backActionClick = {
                onAction(AddMenuProduct.Action.OnBackClick)
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
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 16.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.name,
                            labelStringId = R.string.hint_edit_menu_product_name,
                            onValueChange = { name ->
                                onAction(AddMenuProduct.Action.OnNameTextChanged(name))
                            },
                            errorMessageId = state.nameError,
                            enabled = !state.isLoadingButton
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.newPrice,
                            labelStringId = R.string.hint_edit_menu_product_new_price,
                            onValueChange = { newPrice ->
                                onAction(AddMenuProduct.Action.OnNewPriceTextChanged(newPrice))
                            },
                            errorMessageId = state.newPriceError,
                            enabled = !state.isLoadingButton,
                            keyboardType = KeyboardType.Number
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.oldPrice,
                            labelStringId = R.string.hint_edit_menu_product_old_price,
                            onValueChange = { oldPrice ->
                                onAction(AddMenuProduct.Action.OnOldPriceTextChanged(oldPrice))
                            },
                            enabled = !state.isLoadingButton,
                            keyboardType = KeyboardType.Number
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            AdminTextField(
                                modifier = Modifier.weight(0.6f),
                                value = state.nutrition,
                                labelStringId = R.string.hint_edit_menu_product_nutrition,
                                onValueChange = { nutrition ->
                                    onAction(AddMenuProduct.Action.OnNutritionTextChanged(nutrition))
                                },
                                enabled = !state.isLoadingButton,
                                keyboardType = KeyboardType.Number
                            )
                        }

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.description,
                            labelStringId = R.string.hint_edit_menu_product_description,
                            imeAction = ImeAction.None,
                            onValueChange = { description ->
                                onAction(AddMenuProduct.Action.OnDescriptionTextChanged(description))
                            },
                            maxLines = 20,
                            errorMessageId = state.descriptionError,
                            enabled = !state.isLoadingButton
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.comboDescription,
                            labelStringId = R.string.hint_edit_menu_product_combo_description,
                            imeAction = ImeAction.None,
                            onValueChange = { comboDescription ->
                                onAction(
                                    AddMenuProduct.Action.OnComboDescriptionTextChanged(
                                        comboDescription
                                    )
                                )
                            },
                            maxLines = 20,
                            enabled = !state.isLoadingButton
                        )
                    }
                }

                /*        SwitcherCard(
                            modifier = Modifier.padding(top = 8.dp),
                            checked = state.isVisible,
                            onCheckChanged = onVisibleChanged,
                            labelStringId = R.string.title_edit_menu_product_is_visible,
                            enabled = !state.isLoadingButton
                        )*/

                Spacer(modifier = Modifier.height(AdminTheme.dimensions.scrollScreenBottomSpace))
            }
        }
    }

    @Composable
    override fun mapState(state: AddMenuProduct.ViewDataState): AddMenuProductViewState {
        return AddMenuProductViewState(
            throwable = state.throwable,
            name = state.name,
            nameError = null,
            newPrice = state.newPrice,
            newPriceError = null,
            oldPrice = state.oldPrice,
            description = state.description,
            descriptionError = null,
            nutrition = state.nutrition,
            comboDescription = state.comboDescription,
            isLoadingButton = false
        )
    }

    @Preview(showSystemUi = true)
    @Composable
    fun AddMenuProductScreenPreview() {
        AdminTheme {
            AddMenuProductScreen(
                state = AddMenuProductViewState(
                    name = "",
                    nameError = null,
                    newPrice = "",
                    newPriceError = null,
                    oldPrice = "",
                    description = "",
                    descriptionError = null,
                    nutrition = "",
                    comboDescription = "",
                    isLoadingButton = false,
                    throwable = null
                ),
                onAction = {}
            )
        }
    }

    override fun handleEvent(event: AddMenuProduct.Event) {
        when (event) {
            AddMenuProduct.Event.Back -> {
                findNavController().popBackStack()
            }
        }
    }

}
