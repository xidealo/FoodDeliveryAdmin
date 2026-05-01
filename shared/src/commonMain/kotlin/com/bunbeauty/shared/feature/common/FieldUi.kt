package com.bunbeauty.shared.feature.common

abstract class FieldUi<T> {
    abstract val value: T
    abstract val isError: Boolean
}
