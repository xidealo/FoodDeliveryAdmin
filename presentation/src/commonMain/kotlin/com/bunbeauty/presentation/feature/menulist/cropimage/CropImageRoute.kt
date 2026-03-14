package com.bunbeauty.presentation.feature.menulist.cropimage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry

@Composable
fun CropImageRouteScreen(
    viewModel: CropImageViewModel,
    goBack: () -> Unit,
    backStackEntry: NavBackStackEntry,
) {
    val uri = backStackEntry.arguments

    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: CropImage.Action ->
                viewModel.onAction(event)
            }
        }
}

//    var cropImageView: CropImageView? by remember { mutableStateOf(null) }
//
//    val effects by viewModel.events.collectAsStateWithLifecycle()
//    val consumeEffects =
//        remember {
//            {
//                viewModel.consumeEvents(effects)
//            }
//        }
//
//    CropImageEffect(
//        effects = effects,
//        consumeEffects = consumeEffects,
//        goBack = goBack,
//        cropImageView = cropImageView,
//    )
//
//    CropImageScreen(
//        state = viewState,
//        onAction = onAction,
//        cropImageView = cropImageView,
//        onCropImageViewCreated = { imageView ->
//            cropImageView = imageView
//            onAction(CropImage.Action.SetImageUrl(uri = uri))
//        },
//    )
// }
//
// @Composable
// private fun CropImageEffect(
//    effects: List<CropImage.Event>,
//    goBack: () -> Unit,
//    consumeEffects: () -> Unit,
//    cropImageView: CropImageView?,
// ) {
//    LaunchedEffect(effects) {
//        effects.forEach { effect ->
//            when (effect) {
//                CropImage.Event.GoBack -> {
//                    goBack()
//                }
//
//                CropImage.Event.CropImage -> {
//                    cropImageView?.cropAndSaveImage()
//                }
//            }
//        }
//        consumeEffects()
//    }
// }
//
// @Composable
// private fun CropImageScreen(
//    state: CropImage.DataState,
//    onAction: (CropImage.Action) -> Unit,
//    cropImageView: CropImageView?,
//    onCropImageViewCreated: (CropImageView) -> Unit,
// ) {
//    AdminScaffold(
//        title = stringResource(Res.string.title_crop_image),
//        backActionClick = {
//            onAction(CropImage.Action.BackClick)
//        },
//        actionButton = {
//            Column(
//                modifier = Modifier.padding(horizontal = 16.dp),
//                horizontalAlignment = Alignment.End,
//                verticalArrangement = spacedBy(8.dp),
//            ) {
//                IconButton(
//                    onClick = {
//                        cropImageView?.rotateImage(DEFAULT_ANGEL)
//                    },
//                ) {
//                    Icon(
//                        painter = painterResource(Res.drawable.ic_rotate_right),
//                        contentDescription = null,
//                        tint = AdminTheme.colors.main.primary,
//                    )
//                }
//                IconButton(
//                    onClick = {
//                        cropImageView?.rotateImage(-DEFAULT_ANGEL)
//                    },
//                ) {
//                    Icon(
//                        painter = painterResource(Res.drawable.ic_rotate_left),
//                        contentDescription = null,
//                        tint = AdminTheme.colors.main.primary,
//                    )
//                }
//                LoadingButton(
//                    text = stringResource(Res.string.action_common_save),
//                    isLoading = state.isLoading,
//                    onClick = {
//                        onAction(CropImage.Action.SaveClick)
//                    },
//                )
//            }
//        },
//    ) {
//        Box(
//            modifier =
//                Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//            contentAlignment = Alignment.Center,
//        ) {
//            AndroidView(
//                factory = { context ->
//                    CropImageView(context).apply {
//                        setCropImageOptions(
//                            CropImageOptions(
//                                imageSourceIncludeGallery = false,
//                                imageSourceIncludeCamera = false,
//                                fixAspectRatio = true,
//                                outputCompressFormat = Bitmap.CompressFormat.JPEG,
//                                autoZoomEnabled = true,
//                                outputCompressQuality = ORIGINAL_QUALITY,
//                            ),
//                        )
//                        setOnCropImageCompleteListener { _, result ->
//                            onAction(CropImage.Action.CropImage)
//                        }
//                    }.also { imageView ->
//                        onCropImageViewCreated(imageView)
//                    }
//                },
//                update = { imageView ->
//                    if (state.uri != null) {
//                        imageView.setImageUriAsync(state.uri)
//                    }
//                },
//            )
//        }
//    }
// }

private const val DEFAULT_ANGEL = 90
private const val ORIGINAL_QUALITY = 100
