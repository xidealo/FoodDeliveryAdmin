package com.bunbeauty.shared.designsystem.compose.element.image

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.default_product
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

sealed interface ImageData {
    data class HttpUrl(
        val url: String,
    ) : ImageData

    data class LocalId(
        val id: DrawableResource,
    ) : ImageData
}

@Composable
fun AdminAsyncImage(
    contentDescription: StringResource,
    imageData: ImageData,
    modifier: Modifier = Modifier,
    placeholder: DrawableResource = Res.drawable.default_product,
) {
    AsyncImage(
        modifier = modifier,
        model =
            ImageRequest
                .Builder(LocalPlatformContext.current)
                .data(
                    when (imageData) {
                        is ImageData.HttpUrl -> imageData.url
                        is ImageData.LocalId -> imageData.id
                    },
                ).crossfade(true)
                .build(),
        placeholder = painterResource(placeholder),
        contentDescription = stringResource(contentDescription),
        contentScale = ContentScale.FillWidth,
    )
}

@Composable
fun Modifier.haloGlowAnimated(
    color: Color,
    glowSize: Dp = 28.dp,
): Modifier {
    val transition = rememberInfiniteTransition(label = "halo")

    val alpha by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.85f,
        animationSpec =
            infiniteRepeatable(
                animation =
                    tween(
                        durationMillis = 4000,
                        easing = FastOutSlowInEasing,
                    ),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "alpha",
    )

    return this.drawBehind {
        val glowPx = glowSize.toPx()

        drawCircle(
            brush =
                Brush.radialGradient(
                    colors =
                        listOf(
                            color.copy(alpha = alpha),
                            color.copy(alpha = alpha * 0.4f),
                            Color.Transparent,
                        ),
                    center = center,
                    radius = size.maxDimension / 2 + glowPx,
                ),
            center = center,
            radius = size.maxDimension / 2 + glowPx,
        )
    }
}
