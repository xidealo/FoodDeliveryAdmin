package com.bunbeauty.fooddeliveryadmin.screen.menulist.addmenuproduct

import android.os.Bundle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bunbeauty.common.Constants
import com.bunbeauty.domain.model.Suggestion
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationTextCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.SwitcherCard
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextField
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextFieldWithMenu
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.medium
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.presentation.feature.menulist.addmenuproduct.AddMenuProduct
import com.bunbeauty.presentation.feature.menulist.addmenuproduct.AddMenuProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMenuProductFragment :
    BaseComposeFragment<AddMenuProduct.ViewDataState, AddMenuProductViewState, AddMenuProduct.Action, AddMenuProduct.Event>() {

    override val viewModel: AddMenuProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(AddMenuProduct.Action.Init)
    }

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
            },
            actionButton = {
                LoadingButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textStringId = R.string.action_order_details_save,
                    onClick = {
                        onAction(AddMenuProduct.Action.OnCreateMenuProductClick)
                    },
                    isLoading = state.isLoadingButton
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
                            keyboardType = KeyboardType.Number,
                            errorMessageId = state.oldPriceError
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

                            var expanded by remember {
                                mutableStateOf(false)
                            }

                            AdminTextFieldWithMenu(
                                modifier = Modifier
                                    .weight(0.4f)
                                    .padding(start = 8.dp),
                                expanded = expanded,
                                onExpandedChange = { value ->
                                    expanded = value
                                },
                                value = state.utils,
                                labelStringId = R.string.hint_edit_menu_product_utils,
                                suggestionsList = stringArrayResource(id = R.array.utilsList)
                                    .mapIndexed { index, util ->
                                        Suggestion(index.toString(), util)
                                    },
                                onSuggestionClick = { suggestion ->
                                    expanded = false
                                    onAction(AddMenuProduct.Action.OnUtilsTextChanged(suggestion.value))
                                },
                                enabled = !state.isLoadingButton
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

                NavigationTextCard(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    hintStringId = state.categoryHint,
                    border = state.categoriesErrorBorder,
                    label = state.categoryLabel,
                    onClick = {
                        onAction(AddMenuProduct.Action.OnSelectCategoriesClick)
                    }
                )

                SwitcherCard(
                    modifier = Modifier.padding(top = 8.dp),
                    checked = state.isVisibleInMenu,
                    onCheckChanged = { isVisible ->
                        onAction(
                            AddMenuProduct.Action.OnVisibleInMenuChangeClick(isVisible = isVisible)
                        )
                    },
                    labelStringId = R.string.title_add_menu_product_is_visible_in_menu,
                    enabled = !state.isLoadingButton
                )

                SwitcherCard(
                    modifier = Modifier.padding(top = 8.dp),
                    checked = state.isVisibleInRecommendation,
                    onCheckChanged = { isVisible ->
                        onAction(
                            AddMenuProduct.Action.OnRecommendationVisibleChangeClick(isVisible = isVisible)
                        )
                    },
                    labelStringId = R.string.title_add_menu_product_is_recommendation,
                    enabled = !state.isLoadingButton
                )

                AdminCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    onClick = {
                        onAction(AddMenuProduct.Action.OnAddPhotoClick)
                    },
                    border = state.photoErrorBorder
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp),
                            painter = painterResource(R.drawable.ic_add_photo),
                            tint = state.photoContainsColor,
                            contentDescription = stringResource(R.string.description_common_navigate)
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp),
                            text = stringResource(id = R.string.title_add_menu_product_add_photo),
                            style = AdminTheme.typography.labelLarge.medium,
                            color = state.photoContainsColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AdminTheme.dimensions.scrollScreenBottomSpace))
            }
        }
    }

    @Composable
    override fun mapState(state: AddMenuProduct.ViewDataState): AddMenuProductViewState {
        return AddMenuProductViewState(
            hasError = state.hasError,
            name = state.name,
            nameError = if (state.hasNameError) {
                R.string.error_add_menu_product_empty_name
            } else {
                null
            },
            newPrice = state.newPrice,
            newPriceError = if (state.hasNewPriceError) {
                R.string.error_add_menu_product_empty_new_price
            } else {
                null
            },
            oldPrice = state.oldPrice,
            oldPriceError = if (state.hasOldPriceError) {
                R.string.error_add_menu_product_old_price_incorrect
            } else {
                null
            },
            description = state.description,
            descriptionError = if (state.hasDescriptionError) {
                R.string.error_add_menu_product_empty_description
            } else {
                null
            },
            nutrition = state.nutrition,
            comboDescription = state.comboDescription,
            isLoadingButton = false,
            utils = state.utils,
            categoryLabel = if (state.getSelectedCategory().isEmpty()) {
                stringResource(id = R.string.title_add_menu_product_categories)
            } else {
                state.getSelectedCategory()
                    .joinToString(" ${Constants.BULLET_SYMBOL} ") { category ->
                        category.category.name
                    }
            },
            categoryHint = if (state.getSelectedCategory().isEmpty()) {
                null
            } else {
                R.string.hint_add_menu_product_categories
            },
            isVisibleInMenu = state.isVisibleInMenu,
            isVisibleInRecommendation = state.isVisibleInRecommendation,
            photoErrorBorder = if (state.hasPhotoLinkError) {
                BorderStroke(width = 2.dp, color = AdminTheme.colors.main.error)
            } else {
                null
            },
            photoContainsColor = if (state.hasPhotoLinkError) {
                AdminTheme.colors.main.error
            } else {
                AdminTheme.colors.main.onSurface
            },
            categoriesErrorBorder = if (state.hasCategoriesError) {
                BorderStroke(width = 2.dp, color = AdminTheme.colors.main.error)
            } else {
                null
            }
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
                    hasError = false,
                    utils = "ss",
                    oldPriceError = null,
                    categoryLabel = "Выбрать категории",
                    categoryHint = R.string.hint_add_menu_product_categories,
                    isVisibleInMenu = false,
                    isVisibleInRecommendation = false,
                    photoErrorBorder = null,
                    categoriesErrorBorder = null,
                    photoContainsColor = AdminTheme.colors.main.onSurface
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

            is AddMenuProduct.Event.OpenSelectCategoriesBottomSheet -> {
            }

            AddMenuProduct.Event.GoToGallery -> {
                findNavController().navigate(AddMenuProductFragmentDirections.toGalleryFragment())
            }
        }
    }
}
