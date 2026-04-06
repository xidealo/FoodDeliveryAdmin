package com.bunbeauty.presentation.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.presentation.designsystem.compose.element.card.NavigationIconCard
import com.bunbeauty.presentation.designsystem.compose.element.card.TextWithHintCard
import com.bunbeauty.presentation.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.presentation.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.feature.profile.component.LogoutBottomSheet
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_common_logout
import fooddeliveryadmin.presentation.generated.resources.action_profile_map
import fooddeliveryadmin.presentation.generated.resources.action_profile_settings
import fooddeliveryadmin.presentation.generated.resources.action_profile_statistic
import fooddeliveryadmin.presentation.generated.resources.ic_point
import fooddeliveryadmin.presentation.generated.resources.ic_settings
import fooddeliveryadmin.presentation.generated.resources.ic_statistic
import fooddeliveryadmin.presentation.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.presentation.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.presentation.generated.resources.title_profile
import fooddeliveryadmin.presentation.generated.resources.version_app
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileRouteScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    showErrorMessage: (String) -> Unit,
    goToSettingsScreen: () -> Unit,
    goToStatisticScreen: () -> Unit,
    goToMapScreen: () -> Unit,
    goToLoginScreen: () -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: Profile.Action ->
                viewModel.onAction(event)
            }
        }

    val effects by viewModel.events.collectAsStateWithLifecycle()
    val consumeEffects =
        remember {
            {
                viewModel.consumeEvents(effects)
            }
        }

    ProfileEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        goToSettingsScreen = goToSettingsScreen,
        goToStatisticScreen = goToStatisticScreen,
        goToMapScreen = goToMapScreen,
        goToLoginScreen = goToLoginScreen,
    )

    ProfileScreen(
        state = viewState,
        onAction = onAction,
    )
}

@Composable
private fun ProfileEffect(
    effects: List<Profile.Event>,
    goToSettingsScreen: () -> Unit,
    goToStatisticScreen: () -> Unit,
    goToMapScreen: () -> Unit,
    goToLoginScreen: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                Profile.Event.OpenSettings -> goToSettingsScreen()

                Profile.Event.OpenStatistic -> goToStatisticScreen()

                Profile.Event.OpenLogin -> goToLoginScreen()

                Profile.Event.OpenMap -> goToMapScreen()
            }
        }
        consumeEffects()
    }
}

@Composable
private fun ProfileScreen(
    state: Profile.DataState,
    onAction: (Profile.Action) -> Unit,
) {
    val viewState = state.toViewState()

    AdminScaffold(
        title = stringResource(Res.string.title_profile),
    ) {
        when (viewState.state) {
            ProfileViewState.State.Loading -> {
                LoadingScreen()
            }

            ProfileViewState.State.Error -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(Profile.Action.UpdateData)
                    },
                )
            }

            is ProfileViewState.State.Success -> {
                SuccessProfileScreen(
                    state = viewState.state,
                    onAction = onAction,
                )

                LogoutBottomSheet(
                    isShown = viewState.state.isShowLogoutBottomSheet,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
private fun SuccessProfileScreen(
    state: ProfileViewState.State.Success,
    onAction: (Profile.Action) -> Unit,
) {
    val appVersion = rememberAppVersion()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 72.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TextWithHintCard(
            hint = state.role,
            label = state.userName,
        )
        NavigationIconCard(
            iconId = Res.drawable.ic_statistic,
            labelStringId = Res.string.action_profile_statistic,
            onClick = {
                onAction(Profile.Action.StatisticClick)
            },
        )
        NavigationIconCard(
            iconId = Res.drawable.ic_point,
            labelStringId = Res.string.action_profile_map,
            onClick = {
                onAction(Profile.Action.MapClick)
            },
        )
        NavigationIconCard(
            iconId = Res.drawable.ic_settings,
            labelStringId = Res.string.action_profile_settings,
            onClick = {
                onAction(Profile.Action.SettingsClick)
            },
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(Res.string.version_app, appVersion),
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        LoadingButton(
            modifier = Modifier
                .padding(top = 8.dp),
            text = stringResource(Res.string.action_common_logout),
            onClick = {
                onAction(Profile.Action.LogoutClick)
            },
            isLoading = state.logoutLoading,
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    AdminTheme {
        ProfileScreen(
            state =
                Profile.DataState(
                    state = Profile.DataState.State.SUCCESS,
                    user =
                        Profile.DataState.User(
                            role = com.bunbeauty.domain.feature.profile.model.UserRole.MANAGER,
                            userName = "UserName",
                        ),
                    acceptOrders = true,
                    showAcceptOrdersConfirmation = false,
                    logoutLoading = false,
                    isShowLogoutBottomSheet = false,
                ),
            onAction = {},
        )
    }
}
