package com.bunbeauty.presentation.state

sealed class ExtendedState<T> {
    class Loading<T> : ExtendedState<T>()
    data class AddedSuccess<T>(val data: T) : ExtendedState<T>()
    data class UpdatedSuccess<T>(val data: T) : ExtendedState<T>()
    class Empty<T> : ExtendedState<T>()
    class Error<T> : ExtendedState<T>()
}
