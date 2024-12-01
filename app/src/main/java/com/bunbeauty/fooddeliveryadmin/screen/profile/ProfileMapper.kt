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
                user?.let { user ->
                    val userRoleStringId = when (user.role) {
                        UserRole.MANAGER -> R.string.hint_profile_manager
                        UserRole.ADMIN -> R.string.hint_profile_admin
                    }
                    ProfileViewState.State.Success(
                        role = stringResource(id = userRoleStringId),
                        userName = user.userName,
                        acceptOrders = acceptOrders,
                        acceptOrdersConfirmation = if (acceptOrders) {
                            ProfileViewState.AcceptOrdersConfirmation(
                                isShown = showAcceptOrdersConfirmation,
                                titleResId = R.string.title_profile_enable_orders,
                                descriptionResId = R.string.msg_profile_enable_orders,
                                buttonResId = R.string.action_profile_enable
                            )
                        } else {
                            ProfileViewState.AcceptOrdersConfirmation(
                                isShown = showAcceptOrdersConfirmation,
                                titleResId = R.string.title_profile_disable_orders,
                                descriptionResId = R.string.msg_profile_disable_orders,
                                buttonResId = R.string.action_profile_disable
                            )
                        },
                        logoutLoading = logoutLoading
                    )
                } ?: ProfileViewState.State.Error
            }

            Profile.DataState.State.ERROR -> ProfileViewState.State.Error
            Profile.DataState.State.LOADING -> ProfileViewState.State.Loading
        }
    )
}
