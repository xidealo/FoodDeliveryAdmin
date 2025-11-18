package com.bunbeauty.fooddeliveryadmin.screen.additionlist.createaddition

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.AdminButtonDefaults
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.button.SecondaryButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.SwitcherCard
import com.bunbeauty.fooddeliveryadmin.compose.element.image.AdminAsyncImage
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextField
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextFieldDefaults.keyboardOptions
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.fooddeliveryadmin.screen.image.ImageFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.createmenuproduct.mapper.toImageFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage.CROPPED_IMAGE_URI_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage.CROP_IMAGE_REQUEST_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage.CropImageLaunchMode
import com.bunbeauty.fooddeliveryadmin.util.Constants.IMAGE
import com.bunbeauty.presentation.feature.additionlist.createaddition.CreateAddition
import com.bunbeauty.presentation.feature.additionlist.createaddition.CreateAdditionViewModel
import com.canhub.cropper.parcelable
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateAdditionFragment :
    BaseComposeFragment<CreateAddition.DataState, CreateAdditionViewState, CreateAddition.Action, CreateAddition.Event>() {
    override val viewModel: CreateAdditionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(CROP_IMAGE_REQUEST_KEY) { _, bundle ->
            val croppedImageUri =
                bundle.parcelable<Uri>(CROPPED_IMAGE_URI_KEY) ?: return@setFragmentResultListener

            viewModel.onAction(
                action =
                    CreateAddition.Action.SetImage(
                        croppedImageUri = croppedImageUri.toString(),
                    ),
            )
        }
    }

    @Composable
    override fun Screen(
        state: CreateAdditionViewState,
        onAction: (CreateAddition.Action) -> Unit,
    ) {
        CreateAdditionScreen(onAction = onAction, state = state)
    }

    @Composable
    fun CreateAdditionScreen(
        state: CreateAdditionViewState,
        onAction: (CreateAddition.Action) -> Unit,
    ) {
        val galleryLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                navigateToCropImage(uri)
            }

        AdminScaffold(
            title = stringResource(R.string.title_create_addition),
            backActionClick = { onAction(CreateAddition.Action.OnBackClick) },
            actionButton = {
                BottomButtons(
                    state = state,
                    onAddPhotoClick = {
                        galleryLauncher.launch(IMAGE)
                    },
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
            ) {
                AdminCard(
                    clickable = false,
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            labelText = stringResource(R.string.hint_create_addition_name),
                            value = state.nameField.value,
                            onValueChange = { name ->
                                onAction(
                                    CreateAddition.Action.EditNameAddition(name),
                                )
                            },
                            errorText = stringResource(state.nameField.errorResId),
                            isError = state.nameField.isError,
                            enabled = !state.isLoading,
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            labelText = stringResource(R.string.hint_create_addition_full_name),
                            value = state.fullName,
                            onValueChange = { fullName ->
                                onAction(
                                    CreateAddition.Action.EditFullNameAddition(fullName),
                                )
                            },
                            maxLines = 20,
                            enabled = !state.isLoading,
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            labelText = stringResource(R.string.hint_create_addition_price),
                            value = state.price,
                            onValueChange = { price ->
                                onAction(CreateAddition.Action.EditPriceAddition(price))
                            },
                            enabled = !state.isLoading,
                            keyboardOptions =
                                keyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                ),
                        )

                        AdminTextField(
                            modifier = Modifier.fillMaxWidth(),
                            labelText = stringResource(R.string.hint_create_addition_tag),
                            value = state.tag,
                            onValueChange = { tag ->
                                onAction(CreateAddition.Action.EditTagAddition(tag = tag))
                            },
                            enabled = !state.isLoading,
                        )
                    }
                }

                SwitcherCard(
                    modifier =
                        Modifier
                            .padding(top = 8.dp),
                    checked = state.isVisible,
                    onCheckChanged = { isVisible ->
                        onAction(
                            CreateAddition.Action.OnVisibleClick(
                                isVisible = isVisible,
                            ),
                        )
                    },
                    text = stringResource(R.string.title_create_addition_is_visible),
                    enabled = !state.isLoading,
                )

                state.imageFieldUi.value?.let { imageData ->
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        AdminAsyncImage(
                            modifier =
                                Modifier
                                    .padding(top = 8.dp)
                                    .clip(shape = RoundedCornerShape(size = 8.dp))
                                    .size(240.dp),
                            imageData = imageData,
                            contentDescription = R.string.description_product,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AdminTheme.dimensions.scrollScreenBottomSpace))
            }
        }
    }

    @Composable
    private fun BottomButtons(
        state: CreateAdditionViewState,
        onAddPhotoClick: () -> Unit,
        onAction: (CreateAddition.Action) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(
            modifier = modifier.padding(horizontal = 16.dp),
            verticalArrangement = spacedBy(8.dp),
        ) {
            SecondaryButton(
                textStringId =
                    if (state.imageFieldUi.isSelected) {
                        R.string.action_common_replace_photo
                    } else {
                        R.string.action_common_add_photo
                    },
                onClick = onAddPhotoClick,
                isError = state.imageFieldUi.isError,
                borderColor =
                    if (state.imageFieldUi.isError) {
                        AdminTheme.colors.main.error
                    } else {
                        AdminTheme.colors.main.primary
                    },
                buttonColors = AdminButtonDefaults.accentSecondaryButtonColors,
                elevated = false,
                isEnabled = !state.isLoading,
            )

            LoadingButton(
                text = stringResource(R.string.action_create_addition_save),
                isLoading = state.isLoading,
                onClick = {
                    onAction(CreateAddition.Action.OnSaveCreateAdditionClick)
                },
            )
        }
    }

    private fun navigateToCropImage(uri: Uri?) {
        uri ?: return

        findNavController()
            .navigate(
                directions =
                    CreateAdditionFragmentDirections.actionCreateAdditionFragmentToCropImageFragment(
                        uri = uri,
                        launchMode = CropImageLaunchMode.ADDITION,
                    ),
            )
    }

    @Composable
    override fun mapState(state: CreateAddition.DataState): CreateAdditionViewState =
        CreateAdditionViewState(
            nameField =
                TextFieldUi(
                    value = state.name,
                    errorResId = R.string.error_create_addition_empty_name,
                    isError = state.hasEditNameError,
                ),
            fullName = state.fullName,
            price = state.price,
            isVisible = state.isVisible,
            isLoading = state.isLoading,
            tag = state.tag,
            imageFieldUi = state.imageField.toImageFieldUi(),
        )

    override fun handleEvent(event: CreateAddition.Event) {
        when (event) {
            is CreateAddition.Event.Back -> {
                findNavController().navigateUp()
            }

            is CreateAddition.Event.ShowCreatedAdditionSuccess -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(R.string.msg_create_addition_updated, event.additionName),
                )
                findNavController().popBackStack()
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun CreateAdditionScreenPreview() {
        AdminTheme {
            CreateAdditionScreen(
                state =
                    CreateAdditionViewState(
                        nameField =
                            TextFieldUi(
                                value = "",
                                errorResId = 0,
                                isError = false,
                            ),
                        fullName = "",
                        price = "2",
                        isVisible = false,
                        isLoading = false,
                        tag = "tag",
                        imageFieldUi =
                            ImageFieldUi(
                                value = null,
                                isError = false,
                            ),
                    ),
                onAction = {},
            )
        }
    }
}
