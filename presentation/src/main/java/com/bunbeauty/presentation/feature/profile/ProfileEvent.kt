package com.bunbeauty.presentation.feature.profile

sealed interface ProfileEvent {
    object OpenLogoutEvent: ProfileEvent
    object OpenLoginEvent: ProfileEvent
}