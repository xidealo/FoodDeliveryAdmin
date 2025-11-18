package com.bunbeauty.presentation.extension

import com.bunbeauty.presentation.state.ExtendedState
import com.bunbeauty.presentation.state.State

fun <T : Any> T.toStateSuccess(): State.Success<T> = State.Success(this)

fun <T : Any> T.toStateAddedSuccess(): ExtendedState.AddedSuccess<T> = ExtendedState.AddedSuccess(this)

fun <T : Any> T.toStateUpdatedSuccess(): ExtendedState.UpdatedSuccess<T> = ExtendedState.UpdatedSuccess(this)

fun <T : Any> T?.toStateNullableSuccess(): State.Success<T?> = State.Success(this)
