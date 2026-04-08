package com.bunbeauty.shared.feature.menulist.cropimage

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_select_photo_save
import fooddeliveryadmin.shared.generated.resources.ic_turn
import fooddeliveryadmin.shared.generated.resources.title_crop_image
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CropImageRouteScreen(
    viewModel: CropImageViewModel,
    uri: String,
    preset: CropImagePreset,
    goBack: () -> Unit,
    onCropSaved: (String) -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: CropImage.Action ->
                viewModel.onAction(event)
            }
        }
    val cropImageController =
        rememberPlatformCropImageController(
            onImageCropped = { croppedImageUri ->
                onCropSaved(croppedImageUri)
                goBack()
            },
        )

    val effects by viewModel.events.collectAsStateWithLifecycle()
    val consumeEffects =
        remember {
            {
                viewModel.consumeEvents(effects)
            }
        }

    LaunchedEffect(uri) {
        onAction(CropImage.Action.SetImageUrl(uri))
    }

    CropImageEffect(
        effects = effects,
        cropImageController = cropImageController,
        consumeEffects = consumeEffects,
        goBack = goBack,
    )

    CropImageScreen(
        state = viewState,
        onAction = onAction,
        cropImageController = cropImageController,
        preset = preset,
    )
}

@Composable
private fun CropImageEffect(
    effects: List<CropImage.Event>,
    cropImageController: PlatformCropImageController,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                CropImage.Event.GoBack -> {
                    goBack()
                }

                CropImage.Event.CropImage -> {
                    cropImageController.cropImage()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun CropImageScreen(
    state: CropImage.DataState,
    preset: CropImagePreset,
    onAction: (CropImage.Action) -> Unit,
    cropImageController: PlatformCropImageController,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_crop_image),
        backActionClick = {
            onAction(CropImage.Action.BackClick)
        },
        actionButton = {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = spacedBy(8.dp),
            ) {
                IconButton(
                    modifier =
                        Modifier.border(
                            width = 2.dp,
                            shape = CircleShape,
                            brush =
                                SolidColor(
                                    value = AdminTheme.colors.main.primary,
                                ),
                        ),
                    colors =
                        IconButtonDefaults.iconButtonColors(
                            containerColor = AdminTheme.colors.main.secondary,
                            contentColor = AdminTheme.colors.main.primary,
                        ),
                    onClick = {
                        cropImageController.rotateImage()
                    },
                ) {
                    Icon(
                        modifier =
                            Modifier
                                .padding(8.dp)
                                .size(24.dp),
                        painter = painterResource(Res.drawable.ic_turn),
                        contentDescription = null,
                    )
                }
                LoadingButton(
                    text = stringResource(Res.string.action_select_photo_save),
                    isLoading = state.isLoading,
                    onClick = {
                        onAction(CropImage.Action.SaveClick)
                    },
                )
            }
        },
    ) {
        state.uri?.let { imageUri ->
            PlatformCropImageView(
                imageUri = imageUri,
                controller = cropImageController,
                preset = preset,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
            )
        }
    }
}
