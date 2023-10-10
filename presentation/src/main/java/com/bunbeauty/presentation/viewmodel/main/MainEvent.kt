package com.bunbeauty.presentation.viewmodel.main

import com.bunbeauty.presentation.viewmodel.base.Event

sealed interface MainEvent: Event {
    class ShowMessageEvent(val message: AdminMessage) : MainEvent
}

enum class AdminMessageType {
    INFO,
    ERROR,
}

data class AdminMessage(
    val type: AdminMessageType,
    val text: String
)