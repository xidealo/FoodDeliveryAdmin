package com.bunbeauty.presentation

sealed class ErrorEvent {

    data class MessageError(val message: String) : ErrorEvent()

    data class FieldError(val key: String, val message: String) : ErrorEvent()
}