package com.bunbeauty.shared.feature.profile

import androidx.compose.runtime.Composable
import com.bunbeauty.domain.feature.profile.model.UserRole
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.hint_profile_admin
import fooddeliveryadmin.shared.generated.resources.hint_profile_manager
import org.jetbrains.compose.resources.stringResource

data class ProfileViewState(
    val state: State,
) : BaseViewState {
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val role: String,
            val userName: String,
            val logoutLoading: Boolean,
            val isShowLogoutBottomSheet: Boolean,
        ) : State
    }
}

@Composable
internal fun Profile.DataState.toViewState(): ProfileViewState =
    ProfileViewState(
        state =
            when (state) {
                Profile.DataState.State.SUCCESS -> {
                    user?.let { user ->
                        val roleStringId =
                            when (user.role) {
                                UserRole.MANAGER -> Res.string.hint_profile_manager
                                UserRole.ADMIN -> Res.string.hint_profile_admin
                            }
                        ProfileViewState.State.Success(
                            role = stringResource(roleStringId),
                            userName = user.userName,
                            logoutLoading = logoutLoading,
                            isShowLogoutBottomSheet = isShowLogoutBottomSheet,
                        )
                    } ?: ProfileViewState.State.Error
                }

                Profile.DataState.State.ERROR -> ProfileViewState.State.Error
                Profile.DataState.State.LOADING -> ProfileViewState.State.Loading
            },
    )
