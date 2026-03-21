package com.bunbeauty.presentation.feature.additionlist.editadditionlist

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.button.AdminButtonDefaults
import com.bunbeauty.presentation.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.presentation.designsystem.compose.element.button.SecondaryButton
import com.bunbeauty.presentation.designsystem.compose.element.card.SwitcherCard
import com.bunbeauty.presentation.designsystem.compose.element.image.AdminAsyncImage
import com.bunbeauty.presentation.designsystem.compose.element.surface.AdminSurface
import com.bunbeauty.presentation.designsystem.compose.element.textfield.AdminTextField
import com.bunbeauty.presentation.designsystem.compose.element.textfield.AdminTextFieldDefaults.keyboardOptions
import com.bunbeauty.presentation.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.presentation.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.feature.additionlist.editadditionlist.state.EditAddition
import com.bunbeauty.presentation.feature.additionlist.editadditionlist.state.EditAdditionViewState
import com.bunbeauty.presentation.feature.common.TextFieldUi
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.ImageFieldUi
import com.bunbeauty.presentation.feature.menulist.editmenuproduct.EditMenuProductViewModel
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_common_add_photo
import fooddeliveryadmin.presentation.generated.resources.action_common_replace_photo
import fooddeliveryadmin.presentation.generated.resources.action_edit_addition_save
import fooddeliveryadmin.presentation.generated.resources.description_product
import fooddeliveryadmin.presentation.generated.resources.error_edit_addition_empty_name
import fooddeliveryadmin.presentation.generated.resources.hint_edit_addition_full_name
import fooddeliveryadmin.presentation.generated.resources.hint_edit_addition_name
import fooddeliveryadmin.presentation.generated.resources.hint_edit_addition_price
import fooddeliveryadmin.presentation.generated.resources.hint_edit_addition_tag
import fooddeliveryadmin.presentation.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.presentation.generated.resources.msg_edit_addition_updated
import fooddeliveryadmin.presentation.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.presentation.generated.resources.title_edit_addition
import fooddeliveryadmin.presentation.generated.resources.title_edit_addition_is_visible
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditAdditionRouteScreen(
    additionUuid: String,
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
) {
    val viewModel: EditAdditionViewModel = koinViewModel(
        parameters = { parametersOf(additionUuid) }
    )

    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: EditAddition.Action ->
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

    EditAdditionEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        goBack = goBack,
    )
    Screen(state = mapState(viewState), onAction = onAction)
}

@Composable
private fun EditAdditionEffect(
    effects: List<EditAddition.Event>,
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                EditAddition.Event.Back -> {
                    goBack()
                }

                is EditAddition.Event.ShowUpdateAdditionSuccess ->
                    showInfoMessage(
                        getString(Res.string.msg_edit_addition_updated, effect.additionName),
                        3,
                    )
            }
        }
        consumeEffects()
    }
}

@Composable
fun Screen(
    state: EditAdditionViewState,
    onAction: (EditAddition.Action) -> Unit,
) {
    when (state.state) {
        EditAdditionViewState.State.Error ->
            ErrorScreen(
                mainTextId = Res.string.title_common_can_not_load_data,
                extraTextId = Res.string.msg_common_check_connection_and_retry,
                onClick = {
                    onAction(EditAddition.Action.InitAddition)
                },
            )

        EditAdditionViewState.State.Loading -> LoadingScreen()
        is EditAdditionViewState.State.Success ->
            EditAdditionScreen(
                onAction = onAction,
                state = state.state,
            )
    }
}

@Composable
fun EditAdditionScreen(
    state: EditAdditionViewState.State.Success,
    onAction: (EditAddition.Action) -> Unit,
) {
//    val galleryLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            navigateToCropImage(uri)
//        }

    AdminScaffold(
        title = stringResource(Res.string.title_edit_addition),
        backActionClick = { onAction(EditAddition.Action.OnBackClick) },
        actionButton = {
            BottomButtons(
                state = state,
                onAddPhotoClick = {
                    // galleryLauncher.launch(IMAGE)
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
            AdminSurface {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    AdminTextField(
                        modifier = Modifier.fillMaxWidth(),
                        labelText = stringResource(Res.string.hint_edit_addition_name),
                        value = state.nameField.value,
                        onValueChange = { name ->
                            onAction(
                                EditAddition.Action.EditNameAddition(name),
                            )
                        },
                        errorText = state.nameField.errorRes,
                        isError = state.nameField.isError,
                        enabled = !state.isLoading,
                    )

                    AdminTextField(
                        modifier = Modifier.fillMaxWidth(),
                        labelText = stringResource(Res.string.hint_edit_addition_full_name),
                        value = state.fullName,
                        onValueChange = { fullName ->
                            onAction(
                                EditAddition.Action.EditFullNameAddition(fullName),
                            )
                        },
                        maxLines = 20,
                        enabled = !state.isLoading,
                    )

                    AdminTextField(
                        modifier = Modifier.fillMaxWidth(),
                        labelText = stringResource(Res.string.hint_edit_addition_price),
                        value = state.price,
                        onValueChange = { price ->
                            onAction(EditAddition.Action.EditPriceAddition(price))
                        },
                        enabled = !state.isLoading,
                        keyboardOptions =
                            keyboardOptions(
                                keyboardType = KeyboardType.Number,
                            ),
                    )

                    AdminTextField(
                        modifier = Modifier.fillMaxWidth(),
                        labelText = stringResource(Res.string.hint_edit_addition_tag),
                        value = state.tag,
                        onValueChange = { tag ->
                            onAction(EditAddition.Action.EditTagAddition(tag = tag))
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
                        EditAddition.Action.OnVisibleClick(
                            isVisible = isVisible,
                        ),
                    )
                },
                text = stringResource(Res.string.title_edit_addition_is_visible),
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
                        contentDescription = Res.string.description_product,
                    )
                }
            }

            Spacer(modifier = Modifier.height(AdminTheme.dimensions.scrollScreenBottomSpace))
        }
    }
}

@Composable
private fun BottomButtons(
    state: EditAdditionViewState.State.Success,
    onAddPhotoClick: () -> Unit,
    onAction: (EditAddition.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = spacedBy(8.dp),
    ) {
        SecondaryButton(
            textStringId =
                if (state.imageFieldUi.isSelected) {
                    Res.string.action_common_replace_photo
                } else {
                    Res.string.action_common_add_photo
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
            text = stringResource(Res.string.action_edit_addition_save),
            isLoading = state.isLoading,
            onClick = {
                onAction(EditAddition.Action.OnSaveEditAdditionClick)
            },
        )
    }
}

//    private fun navigateToCropImage(uri: Uri?) {
//        uri ?: return
//
//        findNavController()
//            .navigate(
//                directions =
//                    EditAdditionFragmentDirections.toCropImageFragment(
//                        uri = uri,
//                        launchMode = CropImageLaunchMode.ADDITION,
//                    ),
//            )
//    }

fun mapState(state: EditAddition.DataState): EditAdditionViewState =
    when (state.state) {
        EditAddition.DataState.State.LOADING ->
            EditAdditionViewState(
                state = EditAdditionViewState.State.Loading,
            )

        EditAddition.DataState.State.ERROR ->
            EditAdditionViewState(
                state = EditAdditionViewState.State.Error,
            )

        EditAddition.DataState.State.SUCCESS ->
            EditAdditionViewState(
                state =
                    EditAdditionViewState.State.Success(
                        nameField =
                            TextFieldUi(
                                value = state.name,
                                errorRes = Res.string.error_edit_addition_empty_name,
                                isError = state.hasEditNameError,
                            ),
                        fullName = state.fullName,
                        price = state.price,
                        isVisible = state.isVisible,
                        isLoading = state.isLoading,
                        tag = state.tag,
                        imageFieldUi = state.imageFieldData,
                    ),
            )
    }

@Preview()
@Composable
fun EditAdditionScreenPreview() {
    AdminTheme {
        EditAdditionScreen(
            state =
                EditAdditionViewState.State.Success(
                    nameField =
                        TextFieldUi(
                            value = "",
                            errorRes = Res.string.error_edit_addition_empty_name,
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
                            isSelected = false,
                        ),
                ),
            onAction = {},
        )
    }
}
