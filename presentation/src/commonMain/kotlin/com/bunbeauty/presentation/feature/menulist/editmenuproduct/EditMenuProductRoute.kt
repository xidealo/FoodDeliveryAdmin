package com.bunbeauty.presentation.feature.menulist.editmenuproduct

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.domain.model.Suggestion
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.button.AdminButtonDefaults
import com.bunbeauty.presentation.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.presentation.designsystem.compose.element.button.SecondaryButton
import com.bunbeauty.presentation.designsystem.compose.element.card.NavigationTextCard
import com.bunbeauty.presentation.designsystem.compose.element.card.SwitcherCard
import com.bunbeauty.presentation.designsystem.compose.element.image.AdminAsyncImage
import com.bunbeauty.presentation.designsystem.compose.element.surface.AdminSurface
import com.bunbeauty.presentation.designsystem.compose.element.textfield.AdminTextField
import com.bunbeauty.presentation.designsystem.compose.element.textfield.AdminTextFieldDefaults
import com.bunbeauty.presentation.designsystem.compose.element.textfield.AdminTextFieldWithMenu
import com.bunbeauty.presentation.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.presentation.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.navigation.NavStateHandleParameters.SELECTED_CATEGORY_UUID_LIST
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_common_add_photo
import fooddeliveryadmin.presentation.generated.resources.action_common_menu_product_recommend
import fooddeliveryadmin.presentation.generated.resources.action_common_menu_product_show_in_menu
import fooddeliveryadmin.presentation.generated.resources.action_common_replace_photo
import fooddeliveryadmin.presentation.generated.resources.action_order_details_save
import fooddeliveryadmin.presentation.generated.resources.array_common_menu_product_units
import fooddeliveryadmin.presentation.generated.resources.description_product
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_combo_description
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_description
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_name
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_new_price
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_nutrition
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_old_price
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_units
import fooddeliveryadmin.presentation.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.presentation.generated.resources.title_common_can_not_load_data
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private const val IMAGE = "image/*"

@Composable
fun EditMenuProductRouteScreen(
    menuProductUuid: String,
    showInfoMessage: (String, Int) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    goToCategoryList: (List<String>) -> Unit,
    goToAdditionList: (String) -> Unit,
    goToCropImage: (String) -> Unit,
    savedStateHandle: SavedStateHandle
) {

    val viewModel: EditMenuProductViewModel = koinViewModel(
        parameters = { parametersOf(menuProductUuid) }
    )

    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: EditMenuProduct.Action ->
                viewModel.onAction(event)
            }
        }

    val effects by viewModel.events.collectAsStateWithLifecycle()
    val consumeEffects =
        remember {
            {
                viewModel.consumeEvents(effects)
            }
        }

    LaunchedEffect(Unit) {
        savedStateHandle.getStateFlow(
            SELECTED_CATEGORY_UUID_LIST,
            viewState.categoriesField.value.map { it.category.uuid }
        ).collect {
            onAction(EditMenuProduct.Action.SelectCategories(it))
        }
    }

//    val galleryLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            uri?.let {
//                goToCropImage(it.toString())
//            }
//        }

    EditMenuProductEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        showErrorMessage = showErrorMessage,
        goBack = goBack,
        goToCategoryList = goToCategoryList,
        goToAdditionList = goToAdditionList,
    )

    EditMenuProductScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        addPhotoClick = {
            //  galleryLauncher.launch(IMAGE)
        },
    )
}

@Composable
private fun EditMenuProductEffect(
    effects: List<EditMenuProduct.Event>,
    showInfoMessage: (String, Int) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
    goToCategoryList: (List<String>) -> Unit,
    goToAdditionList: (String) -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                EditMenuProduct.Event.NavigateBack -> {
                    goBack()
                }

                is EditMenuProduct.Event.NavigateToCategoryList -> {
                    goToCategoryList(effect.selectedCategoryList)
                }

                is EditMenuProduct.Event.NavigateToAdditionList -> {
                    goToAdditionList(effect.menuProductUuid)
                }

                is EditMenuProduct.Event.ShowUpdateProductSuccess -> {
                    showInfoMessage(
                        "Продукт обновлен: ${effect.productName}",
                        2000,
                    )
                    goBack()
                }

                EditMenuProduct.Event.ShowImageUploadingFailed -> {
                    showErrorMessage("Не удалось загрузить изображение")
                }

                EditMenuProduct.Event.ShowSomethingWentWrong -> {
                    showErrorMessage("Что-то пошло не так")
                }

                EditMenuProduct.Event.ShowEmptyPhoto -> {
                    showErrorMessage("Добавьте фотографию")
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun EditMenuProductScreen(
    state: EditMenuProductViewState,
    onAction: (EditMenuProduct.Action) -> Unit,
    addPhotoClick: () -> Unit,
) {
    AdminScaffold(
        title = state.title,
        backActionClick = {
            onAction(EditMenuProduct.Action.BackClick)
        },
        actionButton = {
            when (state.state) {
                is EditMenuProductViewState.State.Success -> {
                    BottomButtons(
                        state = state.state,
                        addPhotoClick = addPhotoClick,
                        onAction = onAction,
                    )
                }

                else -> Unit
            }
        },
    ) {
        when (state.state) {
            EditMenuProductViewState.State.Error -> {
                EditMenuProductErrorScreen(
                    onClick = {
                        onAction(EditMenuProduct.Action.BackClick)
                    },
                )
            }

            EditMenuProductViewState.State.Loading -> {
                LoadingScreen()
            }

            is EditMenuProductViewState.State.Success -> {
                EditMenuProductSuccessScreen(
                    state = state.state,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
private fun BottomButtons(
    modifier: Modifier = Modifier,
    state: EditMenuProductViewState.State.Success,
    addPhotoClick: () -> Unit,
    onAction: (EditMenuProduct.Action) -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SecondaryButton(
            textStringId =
                if (state.imageField.isSelected) {
                    Res.string.action_common_replace_photo
                } else {
                    Res.string.action_common_add_photo
                },
            onClick = addPhotoClick,
            isError = state.imageField.isError,
            borderColor =
                if (state.imageField.isError) {
                    AdminTheme.colors.main.error
                } else {
                    AdminTheme.colors.main.primary
                },
            buttonColors = AdminButtonDefaults.accentSecondaryButtonColors,
            elevated = false,
        )
        LoadingButton(
            text = stringResource(Res.string.action_order_details_save),
            isLoading = state.sendingToServer,
            onClick = {
                onAction(EditMenuProduct.Action.SaveMenuProductClick)
            },
        )
    }
}

@Composable
private fun EditMenuProductSuccessScreen(
    state: EditMenuProductViewState.State.Success,
    onAction: (EditMenuProduct.Action) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TextFieldsCard(
            state = state,
            onAction = onAction,
        )
        NavigationTextCard(
            labelText = state.categoriesField.labelResId,
            valueText = state.categoriesField.value,
            isError = state.categoriesField.isError,
            errorText = state.categoriesField.errorResId,
            onClick = {
                onAction(EditMenuProduct.Action.CategoriesClick)
            },
        )

        NavigationTextCard(
            labelText = state.additionListField.labelResId,
            valueText = state.additionListField.value,
            isError = state.additionListField.isError,
            errorText = state.additionListField.errorResId,
            onClick = {
                onAction(EditMenuProduct.Action.AdditionListClick)
            },
        )

        SwitcherCard(
            text = stringResource(Res.string.action_common_menu_product_show_in_menu),
            checked = state.isVisibleInMenu,
            onCheckChanged = {
                onAction(EditMenuProduct.Action.ToggleVisibilityInMenu)
            },
            enabled = !state.sendingToServer,
        )
        SwitcherCard(
            checked = state.isVisibleInRecommendation,
            onCheckChanged = {
                onAction(EditMenuProduct.Action.ToggleVisibilityInRecommendations)
            },
            text = stringResource(Res.string.action_common_menu_product_recommend),
            enabled = !state.sendingToServer,
        )
        state.imageField.value?.let { imageData ->
           AdminAsyncImage(
               modifier =
                   Modifier
                       .fillMaxWidth()
                       .clip(RoundedCornerShape(8.dp)),
               imageData = imageData,
               contentDescription = Res.string.description_product,
           )
        }

        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
fun TextFieldsCard(
    state: EditMenuProductViewState.State.Success,
    onAction: (EditMenuProduct.Action) -> Unit,
) {
    AdminSurface {
        Column(
            modifier =
                Modifier
                    .padding(
                        top = 8.dp,
                        bottom = 16.dp,
                    ).padding(horizontal = 16.dp),
        ) {
            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(Res.string.hint_common_menu_product_name),
                value = state.nameField.value,
                onValueChange = { name ->
                    onAction(
                        EditMenuProduct.Action.ChangeNameText(name = name),
                    )
                },
                isError = state.nameField.isError,
                errorText = state.nameField.errorResId,
                enabled = !state.sendingToServer,
            )

            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(Res.string.hint_common_menu_product_new_price),
                value = state.newPriceField.value,
                onValueChange = { newPrice ->
                    onAction(
                        EditMenuProduct.Action.ChangeNewPriceText(newPrice = newPrice),
                    )
                },
                keyboardOptions =
                    AdminTextFieldDefaults.keyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                isError = state.newPriceField.isError,
                errorText = state.newPriceField.errorResId,
                enabled = !state.sendingToServer,
                trailingIcon = {
                    AdminTextFieldDefaults.RubleSymbol()
                },
            )

            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(Res.string.hint_common_menu_product_old_price),
                value = state.oldPriceField.value,
                onValueChange = { oldPrice ->
                    onAction(
                        EditMenuProduct.Action.ChangeOldPriceText(oldPrice = oldPrice),
                    )
                },
                keyboardOptions =
                    AdminTextFieldDefaults.keyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                isError = state.oldPriceField.isError,
                errorText = state.oldPriceField.errorResId,
                enabled = !state.sendingToServer,
                trailingIcon = {
                    AdminTextFieldDefaults.RubleSymbol()
                },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                AdminTextField(
                    modifier = Modifier.weight(0.6f),
                    labelText = stringResource(Res.string.hint_common_menu_product_nutrition),
                    value = state.nutritionField.value,
                    onValueChange = { nutrition ->
                        onAction(
                            EditMenuProduct.Action.ChangeNutritionText(nutrition = nutrition),
                        )
                    },
                    keyboardOptions =
                        AdminTextFieldDefaults.keyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                    errorText = state.nutritionField.errorResId,
                    isError = state.nutritionField.isError,
                    enabled = !state.sendingToServer,
                )

                var expanded by remember {
                    mutableStateOf(false)
                }

                AdminTextFieldWithMenu(
                    modifier =
                        Modifier
                            .weight(0.4f)
                            .padding(start = 8.dp),
                    expanded = expanded,
                    onExpandedChange = { value ->
                        expanded = value
                    },
                    labelText = stringResource(Res.string.hint_common_menu_product_units),
                    value = state.utils,
                    suggestionsList =
                        stringArrayResource(resource = Res.array.array_common_menu_product_units)
                            .mapIndexed { index, util ->
                                Suggestion(index.toString(), util)
                            },
                    onSuggestionClick = { suggestion ->
                        expanded = false
                        onAction(
                            EditMenuProduct.Action.ChangeUtilsText(units = suggestion.value),
                        )
                    },
                    enabled = !state.sendingToServer,
                )
            }

            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(Res.string.hint_common_menu_product_description),
                value = state.descriptionField.value,
                onValueChange = { description ->
                    onAction(
                        EditMenuProduct.Action.ChangeDescriptionText(description = description),
                    )
                },
                keyboardOptions =
                    AdminTextFieldDefaults.keyboardOptions(
                        imeAction = ImeAction.None,
                    ),
                maxLines = 20,
                isError = state.descriptionField.isError,
                errorText = state.descriptionField.errorResId,
                enabled = !state.sendingToServer,
            )

            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(Res.string.hint_common_menu_product_combo_description),
                value = state.comboDescription,
                onValueChange = { comboDescription ->
                    onAction(
                        EditMenuProduct.Action.ChangeComboDescriptionText(comboDescription = comboDescription),
                    )
                },
                keyboardOptions =
                    AdminTextFieldDefaults.keyboardOptions(
                        imeAction = ImeAction.None,
                    ),
                maxLines = 20,
                enabled = !state.sendingToServer,
            )
        }
    }
}

@Composable
private fun EditMenuProductErrorScreen(onClick: () -> Unit) {
    ErrorScreen(
        mainTextId = Res.string.title_common_can_not_load_data,
        extraTextId = Res.string.msg_common_check_connection_and_retry,
        onClick = onClick,
    )
}
