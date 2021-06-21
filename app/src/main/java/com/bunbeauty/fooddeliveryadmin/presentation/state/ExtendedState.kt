package com.bunbeauty.fooddeliveryadmin.presentation.state

sealed class ExtendedState<T> {
    class Loading<T> : ExtendedState<T>()
    data class AddedSuccess<T>(val data: T) : ExtendedState<T>()
    data class UpdatedSuccess<T>(val data: T) : ExtendedState<T>()
    class Empty<T> : ExtendedState<T>()
    data class Error<T>(val message: String) : ExtendedState<T>()
}