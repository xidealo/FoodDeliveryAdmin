package com.bunbeauty.presentation.feature.menulist.createmenuproduct

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
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
import fooddeliveryadmin.presentation.generated.resources.error_common_something_went_wrong
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_combo_description
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_description
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_name
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_new_price
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_nutrition
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_old_price
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_units
import fooddeliveryadmin.presentation.generated.resources.title_create_menu_product_new_product
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val IMAGE = "image/*"

@Composable
fun CreateMenuProductRouteScreen(
    viewModel: CreateMenuProductViewModel = koinViewModel(),
    showInfoMessage: (String, Int) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    goToCategoryList: (List<String>) -> Unit,
    goToCropImage: (String) -> Unit,
    backStackEntry: NavBackStackEntry,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: CreateMenuProduct.Action ->
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

    //  val galleryLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            uri?.let {
//                goToCropImage(it.toString())
//            }

//        }

    LaunchedEffect(Unit) {
        backStackEntry.savedStateHandle.getStateFlow(
            SELECTED_CATEGORY_UUID_LIST,
            viewState.categoriesField.value.map { it.category.uuid }
        ).collect {
            onAction(CreateMenuProduct.Action.SelectCategories(it))
        }
    }
    CreateMenuProductEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        showErrorMessage = showErrorMessage,
        goBack = goBack,
        goToCategoryList = goToCategoryList,
    )

    CreateMenuProductScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        addPhotoClick = {
            //  galleryLauncher.launch(IMAGE)
        },
    )
}

@Composable
private fun CreateMenuProductEffect(
    effects: List<CreateMenuProduct.Event>,
    showInfoMessage: (String, Int) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
    goToCategoryList: (List<String>) -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                CreateMenuProduct.Event.NavigateBack -> {
                    goBack()
                }

                is CreateMenuProduct.Event.NavigateToCategoryList -> {
                    goToCategoryList(effect.selectedCategoryList)
                }

                is CreateMenuProduct.Event.ShowMenuProductCreated -> {
                    showInfoMessage(
                        effect.menuProductName,
                        2000,
                    )
                    goBack()
                }

                CreateMenuProduct.Event.ShowSomethingWentWrong -> {
                    showErrorMessage(getString(Res.string.error_common_something_went_wrong))
                }

                CreateMenuProduct.Event.ShowImageUploadingFailed -> {
                    showErrorMessage("Не удалось загрузить изображение")
                }

                CreateMenuProduct.Event.ShowEmptyPhoto -> {
                    showErrorMessage("Добавьте фотографию")
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun CreateMenuProductScreen(
    state: CreateMenuProductViewState,
    onAction: (CreateMenuProduct.Action) -> Unit,
    addPhotoClick: () -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_create_menu_product_new_product),
        backActionClick = {
            onAction(CreateMenuProduct.Action.BackClick)
        },
        actionButton = {
            BottomButtons(
                state = state,
                addPhotoClick = addPhotoClick,
                onAction = onAction,
            )
        },
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
                    onAction(CreateMenuProduct.Action.CategoriesClick)
                },
            )
            SwitcherCard(
                checked = state.isVisibleInMenu,
                onCheckChanged = {
                    onAction(CreateMenuProduct.Action.ToggleVisibilityInMenu)
                },
                text = stringResource(Res.string.action_common_menu_product_show_in_menu),
                enabled = !state.sendingToServer,
            )
            SwitcherCard(
                checked = state.isVisibleInRecommendation,
                onCheckChanged = {
                    onAction(CreateMenuProduct.Action.ToggleVisibilityInRecommendations)
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

            Spacer(modifier = Modifier.height(136.dp))
        }
    }
}

@Composable
private fun BottomButtons(
    modifier: Modifier = Modifier,
    state: CreateMenuProductViewState,
    addPhotoClick: () -> Unit,
    onAction: (CreateMenuProduct.Action) -> Unit,
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
        )
        LoadingButton(
            text = stringResource(Res.string.action_order_details_save),
            isLoading = state.sendingToServer,
            onClick = {
                onAction(CreateMenuProduct.Action.CreateMenuProductClick)
            },
        )
    }
}

@Composable
private fun TextFieldsCard(
    state: CreateMenuProductViewState,
    onAction: (CreateMenuProduct.Action) -> Unit,
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
                    onAction(CreateMenuProduct.Action.ChangeNameText(name))
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
                    onAction(CreateMenuProduct.Action.ChangeNewPriceText(newPrice))
                },
                isError = state.newPriceField.isError,
                errorText = state.newPriceField.errorResId,
                enabled = !state.sendingToServer,
                keyboardOptions =
                    AdminTextFieldDefaults.keyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                trailingIcon = {
                    AdminTextFieldDefaults.RubleSymbol()
                },
            )

            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(Res.string.hint_common_menu_product_old_price),
                value = state.oldPriceField.value,
                onValueChange = { oldPrice ->
                    onAction(CreateMenuProduct.Action.ChangeOldPriceText(oldPrice))
                },
                isError = state.oldPriceField.isError,
                errorText = state.oldPriceField.errorResId,
                enabled = !state.sendingToServer,
                keyboardOptions =
                    AdminTextFieldDefaults.keyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                trailingIcon = {
                    AdminTextFieldDefaults.RubleSymbol()
                },
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                AdminTextField(
                    modifier = Modifier.weight(0.6f),
                    labelText = stringResource(Res.string.hint_common_menu_product_nutrition),
                    value = state.nutritionField.value,
                    onValueChange = { nutrition ->
                        onAction(CreateMenuProduct.Action.ChangeNutritionText(nutrition))
                    },
                    enabled = !state.sendingToServer,
                    isError = state.nutritionField.isError,
                    errorText = state.nutritionField.errorResId,
                    keyboardOptions =
                        AdminTextFieldDefaults.keyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
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
                        stringArrayResource(Res.array.array_common_menu_product_units)
                            .mapIndexed { index, util ->
                                Suggestion(index.toString(), util)
                            },
                    onSuggestionClick = { suggestion ->
                        expanded = false
                        onAction(CreateMenuProduct.Action.ChangeUnitsText(suggestion.value))
                    },
                    enabled = !state.sendingToServer,
                )
            }

            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(Res.string.hint_common_menu_product_description),
                value = state.descriptionField.value,
                onValueChange = { description ->
                    onAction(CreateMenuProduct.Action.ChangeDescriptionText(description))
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
                        CreateMenuProduct.Action.ChangeComboDescriptionText(
                            comboDescription,
                        ),
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
