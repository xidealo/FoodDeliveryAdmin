package com.bunbeauty.shared.feature.menulist.common

abstract class FieldData<T> {
    abstract val value: T
    abstract val isError: Boolean
}
