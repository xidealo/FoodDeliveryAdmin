package com.bunbeauty.shared.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.shared.designsystem.compose.element.image.haloGlowAnimated
import com.bunbeauty.shared.designsystem.compose.element.textfield.AdminTextField
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_login_enter
import fooddeliveryadmin.shared.generated.resources.description_logo
import fooddeliveryadmin.shared.generated.resources.error_login_wrong_data
import fooddeliveryadmin.shared.generated.resources.error_login_wrong_login
import fooddeliveryadmin.shared.generated.resources.hint_login_login
import fooddeliveryadmin.shared.generated.resources.hint_login_password
import fooddeliveryadmin.shared.generated.resources.ic_invisible
import fooddeliveryadmin.shared.generated.resources.ic_lock
import fooddeliveryadmin.shared.generated.resources.ic_person
import fooddeliveryadmin.shared.generated.resources.ic_visible
import fooddeliveryadmin.shared.generated.resources.logo
import fooddeliveryadmin.shared.generated.resources.msg_common_check_connection_and_retry
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginRouteScreen(
    viewModel: LoginViewModel = koinViewModel(),
    showErrorMessage: (String) -> Unit,
    goToOrderListScreen: () -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: Login.Action ->
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

    LoginEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showErrorMessage = showErrorMessage,
        goToOrderListScreen = goToOrderListScreen,
    )
    LoginScreen(state = viewState, onAction = onAction)
}

@Composable
private fun LoginEffect(
    effects: List<Login.Event>,
    showErrorMessage: (String) -> Unit,
    goToOrderListScreen: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                Login.Event.OpenOrderListEvent -> {
                    goToOrderListScreen()
                }

                Login.Event.ShowWrongCredentialError -> {
                    showErrorMessage(
                        getString(Res.string.error_login_wrong_data),
                    )
                }

                Login.Event.ShowConnectionError -> {
                    showErrorMessage(
                        getString(Res.string.msg_common_check_connection_and_retry),
                    )
                }

                Login.Event.ShowWrongLoginError -> {
                    showErrorMessage(
                        getString(Res.string.error_login_wrong_login),
                    )
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun LoginScreen(
    state: Login.DataState,
    onAction: (Login.Action) -> Unit,
) {
    when (state.state) {
        Login.DataState.State.LOADING -> LoadingScreen()
        Login.DataState.State.SUCCESS ->
            LoginScreenSuccess(
                state = state,
                onAction = onAction,
            )
    }
}

@Composable
private fun LoginScreenSuccess(
    state: Login.DataState,
    onAction: (Login.Action) -> Unit,
) {
    AdminScaffold(
        actionButton = {
            LoadingButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(Res.string.action_login_enter),
                isLoading = state.startLoginLoading,
                onClick = {
                    onAction(Login.Action.LoginClick)
                },
            )
        },
        backgroundColor = AdminTheme.colors.main.surface,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(
                        horizontal = 16.dp,
                    ).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier =
                    Modifier
                        .padding(top = 24.dp)
                        .size(250.dp)
                        .haloGlowAnimated(
                            color = Color(0xFF494848),
                        ),
                painter = painterResource(Res.drawable.logo),
                contentDescription = stringResource(Res.string.description_logo),
            )

            AdminTextField(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 64.dp),
                labelText = stringResource(Res.string.hint_login_login),
                value = state.username,
                onValueChange = { name ->
                    onAction(Login.Action.ChangeUsername(name))
                },
                leadingIcon = {
                    Icon(
                        painter =
                            painterResource(Res.drawable.ic_person),
                        tint = AdminTheme.colors.main.onSurfaceVariant,
                        contentDescription = null,
                    )
                },
                enabled = !state.startLoginLoading,
            )

            AdminTextField(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 64.dp),
                labelText = stringResource(Res.string.hint_login_password),
                value = state.password,
                onValueChange = { name ->
                    onAction(Login.Action.ChangePassword(password = name))
                },
                enabled = !state.startLoginLoading,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                    ),
                leadingIcon = {
                    Icon(
                        painter =
                            painterResource(Res.drawable.ic_lock),
                        tint = AdminTheme.colors.main.onSurfaceVariant,
                        contentDescription = null,
                    )
                },
                visualTransformation =
                    if (state.isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onAction(Login.Action.ChangeVisiblePassword)
                        },
                    ) {
                        Icon(
                            painter =
                                if (state.isPasswordVisible) {
                                    painterResource(Res.drawable.ic_invisible)
                                } else {
                                    painterResource(Res.drawable.ic_visible)
                                },
                            tint = AdminTheme.colors.main.onSurfaceVariant,
                            contentDescription = null,
                        )
                    }
                },
            )
            Spacer(Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    AdminTheme {
        LoginScreen(
            state =
                Login.DataState(
                    state = Login.DataState.State.SUCCESS,
                    username = "",
                    password = "",
                    isPasswordVisible = false,
                    startLoginLoading = false,
                ),
            onAction = {},
        )
    }
}
