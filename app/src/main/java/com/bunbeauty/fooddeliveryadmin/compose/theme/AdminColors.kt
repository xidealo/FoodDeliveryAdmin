package com.bunbeauty.fooddeliveryadmin.compose.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.Black1
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.Blue1
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.Blue2
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.Cream
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.DarkGrey
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.Green
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.Grey1
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.Grey2
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.Grey3
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.LightGreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.LightRed
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.Purple
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.Red
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.White
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors.Yellow

val LightAdminColors = AdminColors(
    mainColors = MainColors(
        primary = Blue1,
        disabled = Grey1,
        secondary = White,
        background = Cream,
        surface = White,
        error = Red,
        onPrimary = White,
        onDisabled = Grey3,
        onSecondary = Grey3,
        onBackground = Black1,
        onSurface = Black1,
        onSurfaceVariant = Grey2,
        onError = White,
    ),
    orderColors = OrderColors(
        notAccepted = Purple,
        accepted = Blue2,
        preparing = LightRed,
        sentOut = Yellow,
        done = LightGreen,
        delivered = Green,
        canceled = DarkGrey,
        onOrder = White,
    ),
    isLight = true
)

val LocalAdminColors = staticCompositionLocalOf { LightAdminColors }

@Stable
class AdminColors(
    mainColors: MainColors,
    orderColors: OrderColors,
    isLight: Boolean
) {
    var mainColors by mutableStateOf(mainColors)
        private set
    var orderColors by mutableStateOf(orderColors)
        private set
    var isLight by mutableStateOf(isLight)
        internal set

    fun copy(
        mainColors: MainColors = this.mainColors,
        orderColors: OrderColors = this.orderColors,
        isLight: Boolean = this.isLight,
    ) = AdminColors(
        mainColors = mainColors,
        orderColors = orderColors,
        isLight = isLight,
    )

    fun update(other: AdminColors) {
        mainColors.update(other.mainColors)
        orderColors.update(other.orderColors)
        isLight = other.isLight
    }
}

@Stable
class MainColors(
    primary: Color,
    disabled: Color,
    secondary: Color,
    background: Color,
    surface: Color,
    error: Color,
    onPrimary: Color,
    onDisabled: Color,
    onSecondary: Color,
    onBackground: Color,
    onSurface: Color,
    onSurfaceVariant: Color,
    onError: Color,
) {

    var primary by mutableStateOf(primary)
        private set
    var disabled by mutableStateOf(disabled)
        private set
    var secondary by mutableStateOf(secondary)
        private set
    var background by mutableStateOf(background)
        private set
    var surface by mutableStateOf(surface)
        private set
    var error by mutableStateOf(error)
        private set
    var onPrimary by mutableStateOf(onPrimary)
        private set
    var onDisabled by mutableStateOf(onDisabled)
        private set
    var onSecondary by mutableStateOf(onSecondary)
        private set
    var onBackground by mutableStateOf(onBackground)
        private set
    var onSurface by mutableStateOf(onSurface)
        private set
    var onSurfaceVariant by mutableStateOf(onSurfaceVariant)
        private set
    var onError by mutableStateOf(onError)
        private set

    fun copy(
        primary: Color = this.primary,
        disabled: Color = this.disabled,
        secondary: Color = this.secondary,
        background: Color = this.background,
        surface: Color = this.surface,
        error: Color = this.error,
        onPrimary: Color = this.onPrimary,
        onDisabled: Color = this.onDisabled,
        onSecondary: Color = this.onSecondary,
        onBackground: Color = this.onBackground,
        onSurface: Color = this.onSurface,
        onSurfaceVariant: Color = this.onSurfaceVariant,
        onError: Color = this.onError,
    ) = MainColors(
        primary = primary,
        disabled = disabled,
        secondary = secondary,
        background = background,
        surface = surface,
        error = error,
        onPrimary = onPrimary,
        onDisabled = onDisabled,
        onSecondary = onSecondary,
        onBackground = onBackground,
        onSurface = onSurface,
        onSurfaceVariant = onSurfaceVariant,
        onError = onError,
    )

    fun update(other: MainColors) {
        primary = other.primary
        disabled = other.disabled
        secondary = other.secondary
        background = other.background
        surface = other.surface
        error = other.error
        onPrimary = other.onPrimary
        onDisabled = other.onDisabled
        onSecondary = other.onSecondary
        onBackground = other.onBackground
        onSurface = other.onSurface
        onSurfaceVariant = other.onSurfaceVariant
        onError = other.onError
    }
}

@Stable
class OrderColors(
    notAccepted: Color,
    accepted: Color,
    preparing: Color,
    sentOut: Color,
    done: Color,
    delivered: Color,
    canceled: Color,
    onOrder: Color,
) {
    var notAccepted by mutableStateOf(notAccepted)
        private set
    var accepted by mutableStateOf(accepted)
        private set
    var preparing by mutableStateOf(preparing)
        private set
    var sentOut by mutableStateOf(sentOut)
        private set
    var done by mutableStateOf(done)
        private set
    var delivered by mutableStateOf(delivered)
        private set
    var canceled by mutableStateOf(canceled)
        private set
    var onOrder by mutableStateOf(onOrder)
        private set

    fun copy(
        notAccepted: Color = this.notAccepted,
        accepted: Color = this.accepted,
        preparing: Color = this.preparing,
        sentOut: Color = this.sentOut,
        done: Color = this.done,
        delivered: Color = this.delivered,
        canceled: Color = this.canceled,
        onOrder: Color = this.onOrder,
    ) = OrderColors(
        notAccepted = notAccepted,
        accepted = accepted,
        preparing = preparing,
        sentOut = sentOut,
        done = done,
        delivered = delivered,
        canceled = canceled,
        onOrder = onOrder,
    )

    fun update(other: OrderColors) {
        notAccepted = other.notAccepted
        accepted = other.accepted
        preparing = other.preparing
        sentOut = other.sentOut
        done = other.done
        delivered = other.delivered
        canceled = other.canceled
        onOrder = other.onOrder
    }
}
