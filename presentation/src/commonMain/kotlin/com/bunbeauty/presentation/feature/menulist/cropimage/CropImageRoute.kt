package com.bunbeauty.presentation.feature.menulist.cropimage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import coil3.compose.AsyncImage
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_order_details_save
import fooddeliveryadmin.presentation.generated.resources.action_select_photo_save
import fooddeliveryadmin.presentation.generated.resources.ic_turn
import fooddeliveryadmin.presentation.generated.resources.title_crop_image
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

interface ImageCropper {
    suspend fun crop(
        uri: String,
        rotation: Float,
        cropRect: CropRect
    ): String
}

data class CropRect(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)

@Composable
fun CropImageRouteScreen(
    viewModel: CropImageViewModel,
    uri: String,
    goBack: () -> Unit,
) {

    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { event: CropImage.Action ->
            viewModel.onAction(event)
        }
    }

    //var cropImageView: CropImageView? by remember { mutableStateOf(null) }

    val effects by viewModel.events.collectAsStateWithLifecycle()
    val consumeEffects =
        remember {
            {
                viewModel.consumeEvents(effects)
            }
        }

    CropImageEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        goBack = goBack,
    )

    CropImageScreen(
        state = viewState,
        onAction = onAction,
    )
}

@Composable
private fun CropImageEffect(
    effects: List<CropImage.Event>,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
    //cropImageView: CropImageView?,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                CropImage.Event.GoBack -> {
                    goBack()
                }

                CropImage.Event.CropImage -> {
                    // cropImageView?.cropAndSaveImage()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun CropImageScreen(
    state: CropImage.DataState,
    onAction: (CropImage.Action) -> Unit,
) {
    var rotation by remember { mutableStateOf(0f) }
    var cropRect by remember { mutableStateOf(CropRect(0f, 0f, 1f, 1f)) }
    var isLoading by remember { mutableStateOf(false) }

    if (isLoading) {
        LaunchedEffect(Unit) {
            //val result = cropper.crop(imageUri, rotation, cropRect)
            //onResult(result)
        }
    }
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
                        //cropImageView?.rotateImage(DEFAULT_ANGEL)
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
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            // 📷 Изображение
            CroppableImage(
                imageUri = state.uri.orEmpty(),
                rotation = rotation,
                onCropChange = { cropRect = it }
            )

        }
    }
}


@Composable
fun CroppableImage(
    imageUri: String,
    rotation: Float,
    onCropChange: (CropRect) -> Unit
) {
    var rect by remember {
        mutableStateOf(
            CropRect(0.1f, 0.1f, 0.9f, 0.9f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, drag ->
                    change.consume()

                    rect = rect.copy(
                        right = (rect.right + drag.x / size.width).coerceIn(0f, 1f),
                        bottom = (rect.bottom + drag.y / size.height).coerceIn(0f, 1f)
                    )

                    onCropChange(rect)
                }
            }
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationZ = rotation
                }
        )

        // рамка
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.White,
                topLeft = Offset(rect.left * size.width, rect.top * size.height),
                size = Size(
                    (rect.right - rect.left) * size.width,
                    (rect.bottom - rect.top) * size.height
                ),
                style = Stroke(width = 4.dp.toPx())
            )
        }
    }
}

private const val DEFAULT_ANGEL = 90
private const val ORIGINAL_QUALITY = 100
