package com.bunbeauty.fooddeliveryadmin.ui

import org.joda.time.DateTime

sealed class ErrorEvent {

    data class MessageError(
        val time: Long = DateTime.now().millis,
        val message: String
    ) : ErrorEvent()

    data class FieldError(
        val key: String,
        val message: String
    ) : ErrorEvent()
}