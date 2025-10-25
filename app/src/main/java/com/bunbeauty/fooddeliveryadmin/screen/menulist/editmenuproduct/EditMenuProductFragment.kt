package com.bunbeauty.fooddeliveryadmin.screen.menulist.editmenuproduct

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.domain.model.Suggestion
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.AdminButtonDefaults
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.button.SecondaryButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationTextCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.SwitcherCard
import com.bunbeauty.fooddeliveryadmin.compose.element.image.AdminAsyncImage
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextField
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextFieldDefaults.RubleSymbol
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextFieldDefaults.keyboardOptions
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextFieldWithMenu
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.fooddeliveryadmin.screen.image.ImageFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.categorylist.SelectCategoryListFragment.Companion.CATEGORY_LIST_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.categorylist.SelectCategoryListFragment.Companion.CATEGORY_LIST_REQUEST_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.CardFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage.CROPPED_IMAGE_URI_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage.CROP_IMAGE_REQUEST_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage.CropImageLaunchMode
import com.bunbeauty.fooddeliveryadmin.screen.menulist.editmenuproduct.mapper.toEditMenuProductViewState
import com.bunbeauty.fooddeliveryadmin.util.Constants.IMAGE
import com.bunbeauty.presentation.feature.menulist.editmenuproduct.EditMenuProduct
import com.bunbeauty.presentation.feature.menulist.editmenuproduct.EditMenuProductViewModel
import com.canhub.cropper.parcelable
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditMenuProductFragment :
    BaseComposeFragment<EditMenuProduct.DataState, EditMenuProductViewState, EditMenuProduct.Action, EditMenuProduct.Event>() {

    override val viewModel: EditMenuProductViewModel by viewModel()

    private val editMenuProductFragmentArgs: EditMenuProductFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(
            EditMenuProduct.Action.LoadData(
                productUuid = editMenuProductFragmentArgs.menuProductUuid
            )
        )
        setFragmentResultListener(CATEGORY_LIST_REQUEST_KEY) { _, bundle ->
            viewModel.onAction(
                EditMenuProduct.Action.SelectCategories(
                    categoryUuidList = bundle.getStringArrayList(CATEGORY_LIST_KEY)?.toList()
                        ?: emptyList()
                )
            )
        }
        setFragmentResultListener(CROP_IMAGE_REQUEST_KEY) { _, bundle ->
            val croppedImageUri =
                bundle.parcelable<Uri>(CROPPED_IMAGE_URI_KEY) ?: return@setFragmentResultListener

            viewModel.onAction(
                action = EditMenuProduct.Action.SetImage(
                    croppedImageUri = croppedImageUri.toString()
                )
            )
        }
    }

    @Composable
    override fun Screen(
        state: EditMenuProductViewState,
        onAction: (EditMenuProduct.Action) -> Unit
    ) {
        val galleryLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                navigateToCropImage(uri)
            }

        AdminScaffold(
            title = state.title,
            backActionClick = {
                onAction(EditMenuProduct.Action.BackClick)
            },
            actionButton = {
                BottomButtons(
                    state = state.state,
                    onAddPhotoClick = {
                        galleryLauncher.launch(IMAGE)
                    },
                    onAction = onAction
                )
            }
        ) {
            when (state.state) {
                EditMenuProductViewState.State.Error -> {
                    EditMenuProductErrorScreen(
                        onClick = {
                            onAction(
                                EditMenuProduct.Action.LoadData(
                                    productUuid = editMenuProductFragmentArgs.menuProductUuid
                                )
                            )
                        }
                    )
                }

                EditMenuProductViewState.State.Loading -> {
                    LoadingScreen()
                }

                is EditMenuProductViewState.State.Success -> {
                    EditMenuProductSuccessScreen(
                        state = state.state,
                        onAction = onAction
                    )
                }
            }
        }
    }

    @Composable
    override fun mapState(state: EditMenuProduct.DataState): EditMenuProductViewState {
        return state.toEditMenuProductViewState()
    }

    override fun handleEvent(event: EditMenuProduct.Event) {
        when (event) {
            EditMenuProduct.Event.NavigateBack -> {
                findNavController().popBackStack()
            }

            is EditMenuProduct.Event.NavigateToCategoryList -> {
                findNavController().navigate(
                    directions = EditMenuProductFragmentDirections.toSelectCategoryListFragment(
                        selectedCategoryUuidList = event.selectedCategoryList.toTypedArray()
                    )
                )
            }

            is EditMenuProduct.Event.NavigateToAdditionList -> {
                findNavController().navigate(
                    directions = EditMenuProductFragmentDirections
                        .toAdditionGroupForMenuProductListFragment(
                            menuProductUuid = event.menuProductUuid
                        )
                )
            }

            is EditMenuProduct.Event.ShowImageUploadingFailed -> {
                (activity as? MessageHost)?.showErrorMessage(
                    resources.getString(R.string.error_common_menu_product_image_uploading)
                )
            }

            is EditMenuProduct.Event.ShowSomethingWentWrong -> {
                (activity as? MessageHost)?.showErrorMessage(
                    resources.getString(R.string.error_common_something_went_wrong)
                )
            }

            is EditMenuProduct.Event.ShowUpdateProductSuccess -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(R.string.msg_edit_menu_product_updated, event.productName)
                )
                findNavController().popBackStack()
            }

            is EditMenuProduct.Event.ShowEmptyPhoto -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(R.string.error_common_menu_product_empty_photo)
                )
            }
        }
    }

    @Composable
    private fun BottomButtons(
        modifier: Modifier = Modifier,
        state: EditMenuProductViewState.State,
        onAddPhotoClick: () -> Unit,
        onAction: (EditMenuProduct.Action) -> Unit
    ) {
        Column(
            modifier = modifier.padding(horizontal = 16.dp),
            verticalArrangement = spacedBy(8.dp)
        ) {
            if (state is EditMenuProductViewState.State.Success) {
                SecondaryButton(
                    textStringId = if (state.imageField.isSelected) {
                        R.string.action_common_replace_photo
                    } else {
                        R.string.action_common_add_photo
                    },
                    onClick = onAddPhotoClick,
                    isError = state.imageField.isError,
                    borderColor = if (state.imageField.isError) {
                        AdminTheme.colors.main.error
                    } else {
                        AdminTheme.colors.main.primary
                    },
                    buttonColors = AdminButtonDefaults.accentSecondaryButtonColors,
                    elevated = false
                )
                LoadingButton(
                    text = stringResource(R.string.action_order_details_save),
                    isLoading = state.sendingToServer,
                    onClick = {
                        onAction(EditMenuProduct.Action.SaveMenuProductClick)
                    }
                )
            }
        }
    }

    @Composable
    private fun EditMenuProductSuccessScreen(
        state: EditMenuProductViewState.State.Success,
        onAction: (EditMenuProduct.Action) -> Unit
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = spacedBy(8.dp)
        ) {
            TextFieldsCard(
                state = state,
                onAction = onAction
            )
            NavigationTextCard(
                labelText = stringResource(state.categoriesField.labelResId),
                valueText = state.categoriesField.value,
                isError = state.categoriesField.isError,
                errorText = stringResource(state.categoriesField.errorResId),
                onClick = {
                    onAction(EditMenuProduct.Action.CategoriesClick)
                }
            )

            NavigationTextCard(
                labelText = stringResource(state.additionListField.labelResId),
                valueText = state.additionListField.value,
                isError = state.additionListField.isError,
                errorText = stringResource(state.additionListField.errorResId),
                onClick = {
                    onAction(EditMenuProduct.Action.AdditionListClick)
                }
            )

            SwitcherCard(
                text = stringResource(R.string.action_common_menu_product_show_in_menu),
                checked = state.isVisibleInMenu,
                onCheckChanged = {
                    onAction(
                        EditMenuProduct.Action.ToggleVisibilityInMenu
                    )
                },
                enabled = !state.sendingToServer
            )
            SwitcherCard(
                checked = state.isVisibleInRecommendation,
                onCheckChanged = {
                    onAction(EditMenuProduct.Action.ToggleVisibilityInRecommendations)
                },
                text = stringResource(R.string.action_common_menu_product_recommend),
                enabled = !state.sendingToServer
            )
            state.imageField.value?.let { imageData ->
                AdminAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    imageData = imageData,
                    contentDescription = R.string.description_product
                )
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }

    @Composable
    fun TextFieldsCard(
        state: EditMenuProductViewState.State.Success,
        onAction: (EditMenuProduct.Action) -> Unit
    ) {
        AdminCard(clickable = false) {
            Column(
                modifier = Modifier
                    .padding(
                        top = 8.dp,
                        bottom = 16.dp
                    )
                    .padding(horizontal = 16.dp)
            ) {
                AdminTextField(
                    modifier = Modifier.fillMaxWidth(),
                    labelText = stringResource(R.string.hint_common_menu_product_name),
                    value = state.nameField.value,
                    onValueChange = { name ->
                        onAction(
                            EditMenuProduct.Action.ChangeNameText(name = name)
                        )
                    },
                    isError = state.nameField.isError,
                    errorText = stringResource(state.nameField.errorResId),
                    enabled = !state.sendingToServer
                )

                AdminTextField(
                    modifier = Modifier.fillMaxWidth(),
                    labelText = stringResource(R.string.hint_common_menu_product_new_price),
                    value = state.newPriceField.value,
                    onValueChange = { newPrice ->
                        onAction(
                            EditMenuProduct.Action.ChangeNewPriceText(newPrice = newPrice)
                        )
                    },
                    keyboardOptions = keyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    isError = state.newPriceField.isError,
                    errorText = stringResource(state.newPriceField.errorResId),
                    enabled = !state.sendingToServer,
                    trailingIcon = {
                        RubleSymbol()
                    }
                )

                AdminTextField(
                    modifier = Modifier.fillMaxWidth(),
                    labelText = stringResource(R.string.hint_common_menu_product_old_price),
                    value = state.oldPriceField.value,
                    onValueChange = { oldPrice ->
                        onAction(
                            EditMenuProduct.Action.ChangeOldPriceText(oldPrice = oldPrice)
                        )
                    },
                    keyboardOptions = keyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    isError = state.oldPriceField.isError,
                    errorText = stringResource(state.oldPriceField.errorResId),
                    enabled = !state.sendingToServer,
                    trailingIcon = {
                        RubleSymbol()
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AdminTextField(
                        modifier = Modifier.weight(0.6f),
                        labelText = stringResource(R.string.hint_common_menu_product_nutrition),
                        value = state.nutritionField.value,
                        onValueChange = { nutrition ->
                            onAction(
                                EditMenuProduct.Action.ChangeNutritionText(nutrition = nutrition)
                            )
                        },
                        keyboardOptions = keyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        errorText = stringResource(state.nutritionField.errorResId),
                        isError = state.nutritionField.isError,
                        enabled = !state.sendingToServer
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
                        labelText = stringResource(R.string.hint_common_menu_product_units),
                        value = state.utils,
                        suggestionsList = stringArrayResource(id = R.array.array_common_menu_product_units)
                            .mapIndexed { index, util ->
                                Suggestion(
                                    id = index.toString(),
                                    value = util
                                )
                            },
                        onSuggestionClick = { suggestion ->
                            expanded = false
                            onAction(
                                EditMenuProduct.Action.ChangeUtilsText(units = suggestion.value)
                            )
                        },
                        enabled = !state.sendingToServer
                    )
                }

                AdminTextField(
                    modifier = Modifier.fillMaxWidth(),
                    labelText = stringResource(R.string.hint_common_menu_product_description),
                    value = state.descriptionField.value,
                    onValueChange = { description ->
                        onAction(
                            EditMenuProduct.Action.ChangeDescriptionText(description = description)
                        )
                    },
                    keyboardOptions = keyboardOptions(
                        imeAction = ImeAction.None
                    ),
                    maxLines = 20,
                    isError = state.descriptionField.isError,
                    errorText = stringResource(state.descriptionField.errorResId),
                    enabled = !state.sendingToServer
                )

                AdminTextField(
                    modifier = Modifier.fillMaxWidth(),
                    labelText = stringResource(R.string.hint_common_menu_product_combo_description),
                    value = state.comboDescription,
                    onValueChange = { comboDescription ->
                        onAction(
                            EditMenuProduct.Action.ChangeComboDescriptionText(comboDescription = comboDescription)
                        )
                    },
                    keyboardOptions = keyboardOptions(
                        imeAction = ImeAction.None
                    ),
                    maxLines = 20,
                    enabled = !state.sendingToServer
                )
            }
        }
    }

    @Composable
    private fun EditMenuProductErrorScreen(onClick: () -> Unit) {
        ErrorScreen(
            mainTextId = R.string.title_common_can_not_load_data,
            extraTextId = R.string.msg_common_check_connection_and_retry,
            onClick = onClick
        )
    }

    private fun navigateToCropImage(uri: Uri?) {
        uri ?: return

        findNavController()
            .navigate(
                directions = EditMenuProductFragmentDirections.toCropImageFragment(
                    uri = uri,
                    launchMode = CropImageLaunchMode.MENU_PRODUCT
                )
            )
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun EditMenuProductSuccessScreenPreview() {
        AdminTheme {
            Screen(
                state = EditMenuProductViewState(
                    title = "Бургер",
                    state = EditMenuProductViewState.State.Success(
                        nameField = TextFieldUi.empty,
                        newPriceField = TextFieldUi.empty,
                        oldPriceField = TextFieldUi.empty,
                        nutritionField = TextFieldUi.empty,
                        utils = "",
                        descriptionField = TextFieldUi(
                            value = "",
                            isError = false,
                            errorResId = 0
                        ),
                        comboDescription = "",
                        categoriesField = CardFieldUi(
                            labelResId = R.string.hint_common_menu_product_categories,
                            value = "Категория 1 • Категория 2",
                            isError = false,
                            errorResId = 0
                        ),
                        isVisibleInMenu = true,
                        isVisibleInRecommendation = false,
                        imageField = ImageFieldUi(
                            value = null,
                            isError = false
                        ),
                        sendingToServer = false,
                        additionListField = CardFieldUi(
                            labelResId = R.string.hint_common_menu_product_additions,
                            value = "Группа 1 • Группа 2 • Группа 3",
                            isError = false,
                            errorResId = 0
                        )
                    )
                ),
                onAction = {}
            )
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun EditMenuProductErrorScreenPreview() {
        AdminTheme {
            Screen(
                state = EditMenuProductViewState(
                    title = "title",
                    state = EditMenuProductViewState.State.Error
                ),
                onAction = {}
            )
        }
    }
}
