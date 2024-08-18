package com.bunbeauty.fooddeliveryadmin.screen.menulist.createmenuproduct

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
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
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextFieldWithMenu
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.fooddeliveryadmin.screen.menulist.categorylist.CategoryListFragment.Companion.CATEGORY_LIST_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.categorylist.CategoryListFragment.Companion.CATEGORY_LIST_REQUEST_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage.CROPPED_IMAGE_URI_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage.CROP_IMAGE_REQUEST_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage.ORIGINAL_IMAGE_URI_KEY
import com.bunbeauty.presentation.feature.menulist.addmenuproduct.AddMenuProduct
import com.bunbeauty.presentation.feature.menulist.addmenuproduct.AddMenuProductViewModel
import com.canhub.cropper.parcelable
import dagger.hilt.android.AndroidEntryPoint

private const val IMAGE = "image/*"

@AndroidEntryPoint
class CreateMenuProductFragment :
    BaseComposeFragment<AddMenuProduct.DataState, AddMenuProductViewState, AddMenuProduct.Action, AddMenuProduct.Event>() {

    override val viewModel: AddMenuProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(AddMenuProduct.Action.Init)
        setFragmentResultListener(CATEGORY_LIST_REQUEST_KEY) { _, bundle ->
            viewModel.onAction(
                AddMenuProduct.Action.SelectCategoryList(
                    categoryUuidList = bundle.getStringArrayList(CATEGORY_LIST_KEY)?.toList()
                        ?: emptyList()
                )
            )
        }
        setFragmentResultListener(CROP_IMAGE_REQUEST_KEY) { _, bundle ->
            val originalImageUri = bundle.parcelable<Uri>(ORIGINAL_IMAGE_URI_KEY) ?: return@setFragmentResultListener
            val croppedImageUri = bundle.parcelable<Uri>(CROPPED_IMAGE_URI_KEY) ?: return@setFragmentResultListener

            viewModel.onAction(
                action = AddMenuProduct.Action.SetImage(
                    originalImageUri = originalImageUri.toString(),
                    croppedImageUri = croppedImageUri.toString()
                )
            )
        }
    }

    @Composable
    override fun Screen(state: AddMenuProductViewState, onAction: (AddMenuProduct.Action) -> Unit) {
        val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            navigateToCropImage(uri)
        }

        AdminScaffold(
            title = stringResource(R.string.title_add_menu_product),
            backActionClick = {
                onAction(AddMenuProduct.Action.OnBackClick)
            },
            actionButton = {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = spacedBy(8.dp)
                ) {
                    if (state.imageUris == null) {
                        AddPhotoButton(
                            isError = state.imageError,
                            onClick = {
                                galleryLauncher.launch(IMAGE)
                            }
                        )
                    }
                    LoadingButton(
                        textStringId = R.string.action_order_details_save,
                        onClick = {
                            onAction(AddMenuProduct.Action.OnCreateMenuProductClick)
                        },
                        isLoading = state.sendingToServer
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
                TextFieldsCard(
                    state = state,
                    onAction = onAction
                )

                NavigationTextCard(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    hintStringId = state.categoryHint,
                    border = state.categoriesBorder,
                    label = state.categoryLabel,
                    onClick = {
                        onAction(AddMenuProduct.Action.OnShowCategoryListClick)
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
                    text = stringResource(R.string.title_add_menu_product_is_visible_in_menu),
                    enabled = !state.sendingToServer
                )
                SwitcherCard(
                    modifier = Modifier.padding(top = 8.dp),
                    checked = state.isVisibleInRecommendation,
                    onCheckChanged = { isVisible ->
                        onAction(
                            AddMenuProduct.Action.OnRecommendationVisibleChangeClick(isVisible = isVisible)
                        )
                    },
                    text = stringResource(R.string.title_add_menu_product_is_recommendation),
                    enabled = !state.sendingToServer
                )
                PhotoBlock(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    imageUris = state.imageUris,
                    enabled = !state.sendingToServer,
                    onAction = onAction
                )

                Spacer(modifier = Modifier.height(136.dp))
            }
        }
    }

    @Composable
    private fun TextFieldsCard(
        state: AddMenuProductViewState,
        onAction: (AddMenuProduct.Action) -> Unit
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
                    enabled = !state.sendingToServer
                )

                AdminTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.newPrice,
                    labelStringId = R.string.hint_edit_menu_product_new_price,
                    onValueChange = { newPrice ->
                        onAction(AddMenuProduct.Action.OnNewPriceTextChanged(newPrice))
                    },
                    errorMessageId = state.newPriceError,
                    enabled = !state.sendingToServer,
                    keyboardType = KeyboardType.Number
                )

                AdminTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.oldPrice,
                    labelStringId = R.string.hint_edit_menu_product_old_price,
                    onValueChange = { oldPrice ->
                        onAction(AddMenuProduct.Action.OnOldPriceTextChanged(oldPrice))
                    },
                    errorMessageId = state.oldPriceError,
                    enabled = !state.sendingToServer,
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
                        enabled = !state.sendingToServer,
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
                        enabled = !state.sendingToServer
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
                    enabled = !state.sendingToServer
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
                    enabled = !state.sendingToServer
                )
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
    private fun PhotoBlock(
        imageUris: AddMenuProductViewState.ImageUris?,
        enabled: Boolean,
        onAction: (AddMenuProduct.Action) -> Unit,
        modifier: Modifier = Modifier
    ) {
        imageUris ?: return

        Box(modifier = modifier) {
            AdminAsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(enabled = enabled) {
                        navigateToCropImage(imageUris.originalImageUri)
                    },
                imageData = imageUris.croppedImageData,
                contentDescription = R.string.description_product
            )
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = { onAction(AddMenuProduct.Action.OnClearPhotoClick) },
                enabled = enabled
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_clear),
                    contentDescription = null,
                    tint = AdminTheme.colors.main.surface
                )
            }
        }
    }

    @Composable
    override fun mapState(state: AddMenuProduct.DataState): AddMenuProductViewState {
        return state.toAddMenuProductViewState()
    }

    override fun handleEvent(event: AddMenuProduct.Event) {
        when (event) {
            AddMenuProduct.Event.Back -> {
                findNavController().popBackStack()
            }

            is AddMenuProduct.Event.GoToCategoryList -> {
                findNavController().navigate(
                    directions = CreateMenuProductFragmentDirections.toCategoryListFragment(
                        selectedCategoryUuidList = event.selectedCategoryList.toTypedArray()
                    )
                )
            }

            AddMenuProduct.Event.GoToGallery -> {
                findNavController().navigate(
                    directions = CreateMenuProductFragmentDirections.toGalleryFragment()
                )
            }

            is AddMenuProduct.Event.AddedMenuProduct -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(R.string.msg_add_menu_added, event.menuProductName)
                )
                findNavController().popBackStack()
            }

            AddMenuProduct.Event.ShowSomethingWentWrong -> {
                (activity as? MessageHost)?.showErrorMessage(
                    resources.getString(R.string.error_common_something_went_wrong)
                )
            }

            AddMenuProduct.Event.ShowImageUploadingFailed -> {
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
    fun AddMenuProductScreenPreview() {
        AdminTheme {
            Screen(
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
                    sendingToServer = false,
                    utils = "ss",
                    oldPriceError = null,
                    categoryLabel = "Выбрать категории",
                    categoryHint = R.string.hint_add_menu_product_categories,
                    isVisibleInMenu = false,
                    isVisibleInRecommendation = false,
                    categoriesBorder = null,
                    selectableCategoryList = emptyList(),
                    imageUris = null,
                    imageError = false
                ),
                onAction = {}
            )
        }
    }
}
