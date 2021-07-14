package com.bunbeauty.presentation.extension

import com.bunbeauty.presentation.state.ExtendedState
import com.bunbeauty.presentation.state.State

fun <T : Any> T.toStateSuccess(): State.Success<T> {
    return State.Success(this)
}

fun <T : Any> T.toStateAddedSuccess(): ExtendedState.AddedSuccess<T> {
    return ExtendedState.AddedSuccess(this)
}

fun <T : Any> T.toStateUpdatedSuccess(): ExtendedState.UpdatedSuccess<T> {
    return ExtendedState.UpdatedSuccess(this)
}

fun <T : Any> T?.toStateNullableSuccess(): State.Success<T?> {
    return State.Success(this)
}