package com.bunbeauty.presentation.feature.settings

sealed interface SettingsEvent {
    object OpenLogoutEvent: SettingsEvent
    object OpenLoginEvent: SettingsEvent
}