package com.bunbeauty.fooddeliveryadmin.screen.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bunbeauty.domain.feature.profile.model.UserRole
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.presentation.feature.profile.Profile

@Composable
internal fun Profile.DataState.toViewState(): ProfileViewState {
    return ProfileViewState(
        state = when (state) {
            Profile.DataState.State.SUCCESS -> {
                acceptOrders?.let { acceptOrders ->
                    user?.let { user ->
                        val userRoleStringId = when (user.role) {
                            UserRole.MANAGER -> R.string.hint_profile_manager
                            UserRole.ADMIN -> R.string.hint_profile_admin
                        }
                        ProfileViewState.State.Success(
                            role = stringResource(id = userRoleStringId),
                            userName = user.userName,
                            acceptOrders = acceptOrders
                        )
                    } ?: ProfileViewState.State.Error
                } ?: ProfileViewState.State.Error
            }

            Profile.DataState.State.ERROR -> ProfileViewState.State.Error
            Profile.DataState.State.LOADING -> ProfileViewState.State.Loading
        }
    )
}
