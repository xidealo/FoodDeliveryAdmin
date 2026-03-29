package com.bunbeauty.presentation.feature.additionlist.createaddition

import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.Arrangement
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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.button.AdminButtonDefaults
import com.bunbeauty.presentation.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.presentation.designsystem.compose.element.button.SecondaryButton
import com.bunbeauty.presentation.designsystem.compose.element.card.SwitcherCard
import com.bunbeauty.presentation.designsystem.compose.element.image.AdminAsyncImage
import com.bunbeauty.presentation.designsystem.compose.element.surface.AdminSurface
import com.bunbeauty.presentation.designsystem.compose.element.textfield.AdminTextField
import com.bunbeauty.presentation.designsystem.compose.element.textfield.AdminTextFieldDefaults
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.feature.image.rememberImagePickerLauncher
import com.bunbeauty.presentation.feature.additionlist.createaddition.navigation.CreateAdditionScreenDestination
import com.bunbeauty.presentation.navigation.NavStateHandleParameters.CROPPED_IMAGE_URI
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_common_add_photo
import fooddeliveryadmin.presentation.generated.resources.action_common_replace_photo
import fooddeliveryadmin.presentation.generated.resources.action_create_addition_save
import fooddeliveryadmin.presentation.generated.resources.description_product
import fooddeliveryadmin.presentation.generated.resources.hint_create_addition_full_name
import fooddeliveryadmin.presentation.generated.resources.hint_create_addition_name
import fooddeliveryadmin.presentation.generated.resources.hint_create_addition_price
import fooddeliveryadmin.presentation.generated.resources.hint_create_addition_tag
import fooddeliveryadmin.presentation.generated.resources.msg_create_addition_updated
import fooddeliveryadmin.presentation.generated.resources.title_create_addition
import fooddeliveryadmin.presentation.generated.resources.title_create_addition_is_visible
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateAdditionRouteScreen(
    viewModel: CreateAdditionViewModel = koinViewModel(),
    showInfoMessage: (String, Dp) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    goToCropImage: (String) -> Unit,
    backStackEntry: NavBackStackEntry,
) {
    val route = backStackEntry.toRoute<CreateAdditionScreenDestination>()
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: CreateAddition.Action ->
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
    val launchImagePicker =
        rememberImagePickerLauncher { imageUri ->
            goToCropImage(imageUri)
        }

    LaunchedEffect(Unit) {
        backStackEntry.savedStateHandle.getStateFlow<String?>(
            CROPPED_IMAGE_URI,
            null,
        ).collect { croppedImageUri ->
            if (croppedImageUri != null) {
                onAction(CreateAddition.Action.SetImage(croppedImageUri))
                backStackEntry.savedStateHandle.remove<String>(CROPPED_IMAGE_URI)
            }
        }
    }

    CreateAdditionEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        showErrorMessage = showErrorMessage,
        goBack = goBack,
    )

    CreateAdditionScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        addPhotoClick = {
            launchImagePicker()
        },
    )
}

@Composable
private fun CreateAdditionEffect(
    effects: List<CreateAddition.Event>,
    showInfoMessage: (String, Dp) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                CreateAddition.Event.Back -> {
                    goBack()
                }

                is CreateAddition.Event.ShowCreatedAdditionSuccess -> {
                    showInfoMessage(
                        getString(
                            Res.string.msg_create_addition_updated,
                            effect.additionName,
                        ),
                        androidx.compose.material3.ButtonDefaults.MinHeight + 12.dp,
                    )
                    goBack()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
internal fun CreateAdditionScreen(
    state: CreateAdditionViewState,
    onAction: (CreateAddition.Action) -> Unit,
    addPhotoClick: () -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_create_addition),
        backActionClick = {
            onAction(CreateAddition.Action.OnBackClick)
        },
        actionButton = {
            BottomButtons(
                state = state,
                onAddPhotoClick = addPhotoClick,
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
                        labelText = stringResource(Res.string.hint_create_addition_name),
                        value = state.nameField.value,
                        onValueChange = { name ->
                            onAction(
                                CreateAddition.Action.EditNameAddition(name),
                            )
                        },
                        errorText = state.nameField.errorResId,
                        isError = state.nameField.isError,
                        enabled = !state.isLoading,
                    )

                    AdminTextField(
                        modifier = Modifier.fillMaxWidth(),
                        labelText = stringResource(Res.string.hint_create_addition_full_name),
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
                        labelText = stringResource(Res.string.hint_create_addition_price),
                        value = state.price,
                        onValueChange = { price ->
                            onAction(CreateAddition.Action.EditPriceAddition(price))
                        },
                        enabled = !state.isLoading,
                        keyboardOptions =
                            AdminTextFieldDefaults.keyboardOptions(
                                keyboardType = KeyboardType.Number,
                            ),
                    )

                    AdminTextField(
                        modifier = Modifier.fillMaxWidth(),
                        labelText = stringResource(Res.string.hint_create_addition_tag),
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
                text = stringResource(Res.string.title_create_addition_is_visible),
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
    state: CreateAdditionViewState,
    onAddPhotoClick: () -> Unit,
    onAction: (CreateAddition.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
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
            text = stringResource(Res.string.action_create_addition_save),
            isLoading = state.isLoading,
            onClick = {
                onAction(CreateAddition.Action.OnSaveCreateAdditionClick)
            },
        )
    }
}
