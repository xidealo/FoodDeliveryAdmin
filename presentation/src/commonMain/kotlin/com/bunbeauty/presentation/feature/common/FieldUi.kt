package com.bunbeauty.presentation.feature.common

abstract class FieldUi<T> {
    abstract val value: T
    abstract val isError: Boolean
}
