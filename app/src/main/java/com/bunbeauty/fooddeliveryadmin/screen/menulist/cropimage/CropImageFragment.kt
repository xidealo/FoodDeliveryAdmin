package com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.presentation.feature.menulist.cropimage.CropImage
import com.bunbeauty.presentation.feature.menulist.cropimage.CropImageViewModel
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageView.OnCropImageCompleteListener
import dagger.hilt.android.AndroidEntryPoint

const val CROP_IMAGE_REQUEST_KEY = "crop image"
const val ORIGINAL_IMAGE_URI_KEY = "original image uri"
const val CROPPED_IMAGE_URI_KEY = "cropped image uri"

@AndroidEntryPoint
class CropImageFragment :
    BaseComposeFragment<CropImage.DataState, CropImageViewState, CropImage.Action, CropImage.Event>() {

    private val cropImageFragmentArgs: CropImageFragmentArgs by navArgs()

    private var cropImageView: CropImageView? = null

    private val cropImageCompleteListener = OnCropImageCompleteListener { _, result ->
        setFragmentResult(
            CROP_IMAGE_REQUEST_KEY,
            bundleOf(
                ORIGINAL_IMAGE_URI_KEY to result.originalUri,
                CROPPED_IMAGE_URI_KEY to result.uriContent,
            )
        )
        findNavController().popBackStack()
    }

    override val viewModel: CropImageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAction(
            CropImage.Action.SetImageUrl(
                uri = cropImageFragmentArgs.uri.toString()
            )
        )
    }

    @Composable
    override fun mapState(state: CropImage.DataState): CropImageViewState {
        return CropImageViewState(
            isLoading = state.isLoading,
            imageContent = ImageContent(uri = state.uri?.toUri()),
        )
    }

    @Composable
    override fun Screen(state: CropImageViewState, onAction: (CropImage.Action) -> Unit) {
        AdminScaffold(
            title = stringResource(R.string.title_add_menu_product),
            backActionClick = {
                findNavController().popBackStack()
            },
            actionButton = {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = spacedBy(8.dp),
                ) {
                    IconButton(
                        modifier = Modifier.border(
                            width = 2.dp,
                            shape = CircleShape,
                            brush = SolidColor(
                                value = AdminTheme.colors.main.primary
                            )
                        ),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = AdminTheme.colors.main.secondary,
                            contentColor = AdminTheme.colors.main.primary,
                        ),
                        onClick = {
                            cropImageView?.rotateImage(90)
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(24.dp),
                            painter = painterResource(R.drawable.ic_turn),
                            contentDescription = null
                        )
                    }

                    LoadingButton(
                        textStringId = R.string.action_order_details_save,
                        isLoading = state.isLoading,
                        onClick = {
                            onAction(CropImage.Action.SaveClick)
                        }
                    )
                }
            }
        ) {
            CropImageView(imageContent = state.imageContent)
        }
    }

    @Composable
    private fun CropImageView(
        imageContent: ImageContent,
        modifier: Modifier = Modifier,
    ) {
        Log.d("testTag", "CropImageView recompose ${imageContent.uri.hashCode()}")
        val uri = imageContent.uri ?: return

        Box(modifier = modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.align(Alignment.Center),
                factory = { context ->
                    LinearLayout(context).apply {
                        cropImageView = CropImageView(context).apply {
                            setImageCropOptions(CropImageDefaults.options())
                            setImageUriAsync(uri)
                        }.also { view ->
                            view.setOnCropImageCompleteListener(cropImageCompleteListener)
                        }
                        addView(cropImageView)
                    }
                }
            )
        }
    }

    override fun handleEvent(event: CropImage.Event) {
        when (event) {
            is CropImage.Event.CropImage -> {
                cropImageView?.croppedImageAsync(
                    saveCompressFormat = getCompressFormat(),
                    saveCompressQuality = event.compressQuality
                )
            }
        }
    }

    private fun getCompressFormat(): Bitmap.CompressFormat {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            Bitmap.CompressFormat.WEBP
        }
    }

    @Preview
    @Composable
    private fun ScreenPreview() {
        Screen(
            state = CropImageViewState(
                isLoading = false,
                imageContent = ImageContent(
                    uri = null
                )
            ),
            onAction = {}
        )
    }

}