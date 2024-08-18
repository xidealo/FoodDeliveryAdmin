package com.bunbeauty.fooddeliveryadmin.compose.element.image

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bunbeauty.fooddeliveryadmin.R

sealed interface ImageData {
    data class HttpUrl(val url: String): ImageData
    data class LocalUri(val uri: Uri): ImageData
}

@Composable
fun AdminAsyncImage(
    @StringRes contentDescription: Int,
    imageData: ImageData,
    modifier: Modifier = Modifier,
    @DrawableRes placeholder: Int = R.drawable.default_product
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(
                when (imageData) {
                    is ImageData.HttpUrl -> imageData.url
                    is ImageData.LocalUri -> imageData.uri
                }
            )
            .crossfade(true)
            .build(),
        placeholder = painterResource(placeholder),
        contentDescription = stringResource(contentDescription),
        contentScale = ContentScale.FillWidth
    )
}
