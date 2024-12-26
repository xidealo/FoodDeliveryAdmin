package com.bunbeauty.fooddeliveryadmin.screen.profile

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.BuildConfig
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.button.MainButton
import com.bunbeauty.fooddeliveryadmin.compose.element.button.SecondaryButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationIconCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.SwitcherCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.TextWithHintCard
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.fooddeliveryadmin.screen.logout.LogoutBottomSheet
import com.bunbeauty.fooddeliveryadmin.screen.profile.ProfileFragmentDirections.Companion.toCafeListFragment
import com.bunbeauty.fooddeliveryadmin.screen.profile.ProfileFragmentDirections.Companion.toLoginFragment
import com.bunbeauty.fooddeliveryadmin.screen.profile.ProfileFragmentDirections.Companion.toSettingsFragment
import com.bunbeauty.fooddeliveryadmin.screen.profile.ProfileFragmentDirections.Companion.toStatisticFragment
import com.bunbeauty.presentation.feature.profile.Profile
import com.bunbeauty.presentation.feature.profile.ProfileViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment :
    BaseComposeFragment<Profile.DataState, ProfileViewState, Profile.Action, Profile.Event>() {

    override val viewModel: ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAction(Profile.Action.UpdateData)
    }

    @Composable
    override fun mapState(state: Profile.DataState): ProfileViewState {
        return state.toViewState()
    }

    override fun handleEvent(event: Profile.Event) {
        when (event) {
            Profile.Event.OpenSettings -> {
                findNavController().navigateSafe(toSettingsFragment())
            }

            Profile.Event.OpenCafeList -> {
                findNavController().navigateSafe(toCafeListFragment())
            }

            Profile.Event.OpenStatistic -> {
                findNavController().navigateSafe(toStatisticFragment())
            }

            Profile.Event.OpenLogout -> {
                lifecycleScope.launch {
                    LogoutBottomSheet.show(parentFragmentManager)?.let { isConfirmed ->
                        viewModel.onAction(Profile.Action.LogoutConfirm(confirmed = isConfirmed))
                    }
                }
            }

            Profile.Event.OpenLogin -> {
                findNavController().navigateSafe(toLoginFragment())
            }
        }
    }

    @Composable
    override fun Screen(
        state: ProfileViewState,
        onAction: (Profile.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_profile),
            actionButton = {
                if (state.state is ProfileViewState.State.Success) {
                    LoadingButton(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(R.string.action_common_logout),
                        onClick = {
                            onAction(Profile.Action.LogoutClick)
                        },
                        isLoading = state.state.logoutLoading
                    )
                }
            }
        ) {
            when (state.state) {
                ProfileViewState.State.Loading -> {
                    LoadingScreen()
                }

                ProfileViewState.State.Error -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            onAction(Profile.Action.UpdateData)
                        }
                    )
                }

                is ProfileViewState.State.Success -> {
                    SuccessProfileScreen(
                        state = state.state,
                        onAction = onAction
                    )
                }
            }
        }
    }

    @Composable
    private fun SuccessProfileScreen(
        state: ProfileViewState.State.Success,
        onAction: (Profile.Action) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextWithHintCard(
                hint = state.role,
                label = state.userName
            )
            NavigationIconCard(
                iconId = R.drawable.ic_cafe,
                labelStringId = R.string.action_profile_cafes,
                onClick = {
                    onAction(Profile.Action.CafeClick)
                }
            )
            NavigationIconCard(
                iconId = R.drawable.ic_settings,
                labelStringId = R.string.action_profile_settings,
                onClick = {
                    onAction(Profile.Action.SettingsClick)
                }
            )
            NavigationIconCard(
                iconId = R.drawable.ic_statistic,
                labelStringId = R.string.action_profile_statistic,
                onClick = {
                    onAction(Profile.Action.StatisticClick)
                }
            )
            SwitcherCard(
                text = stringResource(R.string.msg_accept_orders),
                hint = stringResource(R.string.msg_accept_orders_description),
                checked = state.acceptOrders,
                onCheckChanged = {
                    onAction(Profile.Action.AcceptOrdersClick)
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = resources.getString(R.string.version_app, BuildConfig.VERSION_NAME),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 72.dp)
            )
        }

        AdminModalBottomSheet(
            title = stringResource(state.acceptOrdersConfirmation.titleResId),
            isShown = state.acceptOrdersConfirmation.isShown,
            onDismissRequest = {
                onAction(Profile.Action.CancelAcceptOrders)
            }
        ) {
            Text(
                text = stringResource(state.acceptOrdersConfirmation.descriptionResId),
                style = AdminTheme.typography.bodyMedium,
                color = AdminTheme.colors.main.onSurface
            )
            MainButton(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(state.acceptOrdersConfirmation.buttonResId),
                onClick = {
                    onAction(Profile.Action.ConfirmAcceptOrders)
                }
            )
            SecondaryButton(
                modifier = Modifier.padding(top = 8.dp),
                textStringId = R.string.action_common_cancel,
                onClick = {
                    onAction(Profile.Action.CancelAcceptOrders)
                }
            )
        }
    }

    @Preview
    @Composable
    private fun ProfileScreenPreview() {
        AdminTheme {
            Screen(
                state = ProfileViewState(
                    state = ProfileViewState.State.Success(
                        role = "Менеджер",
                        userName = "UserName",
                        acceptOrders = true,
                        acceptOrdersConfirmation = ProfileViewState.AcceptOrdersConfirmation(
                            isShown = false,
                            titleResId = 0,
                            descriptionResId = 0,
                            buttonResId = 0
                        ),
                        logoutLoading = false
                    )
                ),
                onAction = {}
            )
        }
    }
}
