package com.bunbeauty.presentation.feature.profile

sealed interface ProfileEvent {
    object OpenSettingsEvent : ProfileEvent
    object OpenCafeListEvent : ProfileEvent
    object OpenStatisticEvent : ProfileEvent
    object OpenLogoutEvent : ProfileEvent
    object OpenLoginEvent : ProfileEvent
}
