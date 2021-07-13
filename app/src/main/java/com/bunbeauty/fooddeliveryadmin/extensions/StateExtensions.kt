package com.bunbeauty.fooddeliveryadmin.extensions

import com.bunbeauty.presentation.view_model.state.ExtendedState
import com.bunbeauty.presentation.view_model.state.State

fun <T : Any> T.toStateSuccess(): com.bunbeauty.presentation.view_model.state.State.Success<T> {
    return com.bunbeauty.presentation.view_model.state.State.Success(this)
}

fun <T : Any> T.toStateAddedSuccess(): com.bunbeauty.presentation.view_model.state.ExtendedState.AddedSuccess<T> {
    return com.bunbeauty.presentation.view_model.state.ExtendedState.AddedSuccess(this)
}

fun <T : Any> T.toStateUpdatedSuccess(): com.bunbeauty.presentation.view_model.state.ExtendedState.UpdatedSuccess<T> {
    return com.bunbeauty.presentation.view_model.state.ExtendedState.UpdatedSuccess(this)
}

fun <T : Any> T?.toStateNullableSuccess(): com.bunbeauty.presentation.view_model.state.State.Success<T?> {
    return com.bunbeauty.presentation.view_model.state.State.Success(this)
}