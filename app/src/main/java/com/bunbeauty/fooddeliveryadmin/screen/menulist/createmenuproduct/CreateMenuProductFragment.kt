package com.bunbeauty.fooddeliveryadmin.screen.menulist.createmenuproduct

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.fooddeliveryadmin.screen.menulist.categorylist.CategoryListFragment.Companion.CATEGORY_LIST_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.categorylist.CategoryListFragment.Companion.CATEGORY_LIST_REQUEST_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.CardFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.createmenuproduct.mapper.toAddMenuProductViewState
import com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage.CROPPED_IMAGE_URI_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage.CROP_IMAGE_REQUEST_KEY
import com.bunbeauty.fooddeliveryadmin.util.Constants.IMAGE
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.CreateMenuProduct
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.CreateMenuProductViewModel
import com.canhub.cropper.parcelable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateMenuProductFragment :
    BaseComposeFragment<CreateMenuProduct.DataState, CreateMenuProductViewState, CreateMenuProduct.Action, CreateMenuProduct.Event>() {

    override val viewModel: CreateMenuProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(CreateMenuProduct.Action.Init)
        setFragmentResultListener(CATEGORY_LIST_REQUEST_KEY) { _, bundle ->
            viewModel.onAction(
                CreateMenuProduct.Action.SelectCategories(
                    categoryUuidList = bundle.getStringArrayList(CATEGORY_LIST_KEY)?.toList()
                        ?: emptyList()
                )
            )
        }
        setFragmentResultListener(CROP_IMAGE_REQUEST_KEY) { _, bundle ->
            val croppedImageUri = bundle.parcelable<Uri>(CROPPED_IMAGE_URI_KEY) ?: return@setFragmentResultListener

            viewModel.onAction(
                action = CreateMenuProduct.Action.SetImage(
                    croppedImageUri = croppedImageUri.toString()
                )
            )
        }
    }

    @Composable
    override fun Screen(state: CreateMenuProductViewState, onAction: (CreateMenuProduct.Action) -> Unit) {
        val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            navigateToCropImage(uri)
        }

        AdminScaffold(
            title = stringResource(R.string.title_create_menu_product_new_product),
            backActionClick = {
                onAction(CreateMenuProduct.Action.BackClick)
            },
            actionButton = {
                BottomButtons(
                    state = state,
                    addPhotoClick = {
                        galleryLauncher.launch(IMAGE)
                    },
                    onAction = onAction
                )
            }
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
                        onAction(CreateMenuProduct.Action.CategoriesClick)
                    }
                )
                SwitcherCard(
                    checked = state.isVisibleInMenu,
                    onCheckChanged = {
                        onAction(CreateMenuProduct.Action.ToggleVisibilityInMenu)
                    },
                    text = stringResource(R.string.action_common_menu_product_show_in_menu),
                    enabled = !state.sendingToServer
                )
                SwitcherCard(
                    checked = state.isVisibleInRecommendation,
                    onCheckChanged = {
                        onAction(CreateMenuProduct.Action.ToggleVisibilityInRecommendations)
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

                Spacer(modifier = Modifier.height(136.dp))
            }
        }
    }

    @Composable
    private fun BottomButtons(
        modifier: Modifier = Modifier,
        state: CreateMenuProductViewState,
        addPhotoClick: () -> Unit,
        onAction: (CreateMenuProduct.Action) -> Unit
    ) {
        Column(
            modifier = modifier.padding(horizontal = 16.dp),
            verticalArrangement = spacedBy(8.dp)
        ) {
            SecondaryButton(
                textStringId = if (state.imageField.isSelected) {
                    R.string.action_common_replace_photo
                } else {
                    R.string.action_common_add_photo
                },
                onClick = addPhotoClick,
                isError = state.imageField.isError,
                borderColor = if (state.imageField.isError) {
                    AdminTheme.colors.main.error
                } else {
                    AdminTheme.colors.main.primary
                },
                buttonColors = AdminButtonDefaults.accentSecondaryButtonColors
            )
            LoadingButton(
                textStringId = R.string.action_order_details_save,
                onClick = {
                    onAction(CreateMenuProduct.Action.CreateMenuProductClick)
                },
                isLoading = state.sendingToServer
            )
        }
    }

    @Composable
    private fun TextFieldsCard(
        state: CreateMenuProductViewState,
        onAction: (CreateMenuProduct.Action) -> Unit
    ) {
        AdminCard(
            clickable = false
        ) {
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
                        onAction(CreateMenuProduct.Action.ChangeNameText(name))
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
                        onAction(CreateMenuProduct.Action.ChangeNewPriceText(newPrice))
                    },
                    isError = state.newPriceField.isError,
                    errorText = stringResource(state.newPriceField.errorResId),
                    enabled = !state.sendingToServer,
                    keyboardOptions = keyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    trailingIcon = {
                        RubleSymbol()
                    }
                )

                AdminTextField(
                    modifier = Modifier.fillMaxWidth(),
                    labelText = stringResource(R.string.hint_common_menu_product_old_price),
                    value = state.oldPriceField.value,
                    onValueChange = { oldPrice ->
                        onAction(CreateMenuProduct.Action.ChangeOldPriceText(oldPrice))
                    },
                    isError = state.oldPriceField.isError,
                    errorText = stringResource(state.oldPriceField.errorResId),
                    enabled = !state.sendingToServer,
                    keyboardOptions = keyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    trailingIcon = {
                        RubleSymbol()
                    }
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    AdminTextField(
                        modifier = Modifier.weight(0.6f),
                        labelText = stringResource(R.string.hint_common_menu_product_nutrition),
                        value = state.nutrition,
                        onValueChange = { nutrition ->
                            onAction(CreateMenuProduct.Action.ChangeNutritionText(nutrition))
                        },
                        enabled = !state.sendingToServer,
                        keyboardOptions = keyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
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
                                Suggestion(index.toString(), util)
                            },
                        onSuggestionClick = { suggestion ->
                            expanded = false
                            onAction(CreateMenuProduct.Action.ChangeUtilsText(suggestion.value))
                        },
                        enabled = !state.sendingToServer
                    )
                }

                AdminTextField(
                    modifier = Modifier.fillMaxWidth(),
                    labelText = stringResource(R.string.hint_common_menu_product_description),
                    value = state.descriptionField.value,
                    onValueChange = { description ->
                        onAction(CreateMenuProduct.Action.ChangeDescriptionText(description))
                    },
                    keyboardOptions = keyboardOptions(
                        imeAction = ImeAction.None,
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
                            CreateMenuProduct.Action.ChangeComboDescriptionText(
                                comboDescription
                            )
                        )
                    },
                    keyboardOptions = keyboardOptions(
                        imeAction = ImeAction.None,
                    ),
                    maxLines = 20,
                    enabled = !state.sendingToServer,
                )
            }
        }
    }

    @Composable
    override fun mapState(state: CreateMenuProduct.DataState): CreateMenuProductViewState {
        return state.toAddMenuProductViewState()
    }

    override fun handleEvent(event: CreateMenuProduct.Event) {
        when (event) {
            CreateMenuProduct.Event.NavigateBack -> {
                findNavController().popBackStack()
            }

            is CreateMenuProduct.Event.NavigateToCategoryList -> {
                findNavController().navigate(
                    directions = CreateMenuProductFragmentDirections.toCategoryListFragment(
                        selectedCategoryUuidList = event.selectedCategoryList.toTypedArray()
                    )
                )
            }

            is CreateMenuProduct.Event.ShowMenuProductCreated -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(R.string.msg_create_menu_product_add_menu_added, event.menuProductName)
                )
                findNavController().popBackStack()
            }

            CreateMenuProduct.Event.ShowImageUploadingFailed -> {
                (activity as? MessageHost)?.showErrorMessage(
                    resources.getString(R.string.error_common_menu_product_image_uploading)
                )
            }

            CreateMenuProduct.Event.ShowSomethingWentWrong -> {
                (activity as? MessageHost)?.showErrorMessage(
                    resources.getString(R.string.error_common_something_went_wrong)
                )
            }
        }
    }

    private fun navigateToCropImage(uri: Uri?) {
        uri ?: return

        findNavController()
            .navigate(
                directions = CreateMenuProductFragmentDirections.toCropImageFragment(
                    uri = uri
                )
            )
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun AddMenuProductScreenPreview() {
        AdminTheme {
            Screen(
                state = CreateMenuProductViewState(
                    nameField = TextFieldUi(
                        value = "",
                        isError = false,
                        errorResId = 0,
                    ),
                    newPriceField = TextFieldUi(
                        value = "",
                        isError = false,
                        errorResId = 0,
                    ),
                    oldPriceField = TextFieldUi(
                        value = "",
                        isError = false,
                        errorResId = 0,
                    ),
                    nutrition = "",
                    utils = "",
                    descriptionField = TextFieldUi(
                        value = "",
                        isError = false,
                        errorResId = 0,
                    ),
                    comboDescription = "",
                    categoriesField = CardFieldUi(
                        labelResId = R.string.hint_common_menu_product_categories,
                        value = "Категория 1 • Категория 2",
                        isError = false,
                        errorResId = 0,
                    ),
                    isVisibleInMenu = true,
                    isVisibleInRecommendation = false,
                    imageField = CreateMenuProductViewState.ImageFieldUi(
                        value = null,
                        isError = false
                    ),
                    sendingToServer = false,
                ),
                onAction = {}
            )
        }
    }
}
