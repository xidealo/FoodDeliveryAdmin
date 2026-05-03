package com.bunbeauty.shared.extension

import com.bunbeauty.shared.state.ExtendedState
import com.bunbeauty.shared.state.State

fun <T : Any> T.toStateSuccess(): State.Success<T> = State.Success(this)

fun <T : Any> T.toStateAddedSuccess(): ExtendedState.AddedSuccess<T> = ExtendedState.AddedSuccess(this)

fun <T : Any> T.toStateUpdatedSuccess(): ExtendedState.UpdatedSuccess<T> = ExtendedState.UpdatedSuccess(this)

fun <T : Any> T?.toStateNullableSuccess(): State.Success<T?> = State.Success(this)
