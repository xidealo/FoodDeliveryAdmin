package com.bunbeauty.fooddeliveryadmin.screen.login

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.image.AdminAsyncImage
import com.bunbeauty.fooddeliveryadmin.compose.element.image.ImageData
import com.bunbeauty.fooddeliveryadmin.compose.element.image.haloGlowAnimated
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextField
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.presentation.feature.login.Login
import com.bunbeauty.presentation.feature.login.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : SingleStateComposeFragment<Login.DataState, Login.Action, Login.Event>() {
    override val viewModel: LoginViewModel by viewModel()

    override fun handleEvent(event: Login.Event) {
        when (event) {
            Login.Event.OpenOrderListEvent -> {
                findNavController().navigateSafe(LoginFragmentDirections.toOrdersFragment())
            }

            Login.Event.ShowWrongCredentialError -> {
                (activity as? MessageHost)?.showErrorMessage(
                    resources.getString(R.string.error_login_wrong_data),
                )
            }

            Login.Event.ShowConnectionError -> {
                (activity as? MessageHost)?.showErrorMessage(
                    resources.getString(R.string.msg_common_check_connection_and_retry),
                )
            }

            Login.Event.ShowWrongLoginError -> {
                (activity as? MessageHost)?.showErrorMessage(
                    resources.getString(R.string.error_login_wrong_login),
                )
            }
        }
    }

    @Composable
    override fun Screen(
        state: Login.DataState,
        onAction: (Login.Action) -> Unit,
    ) {
        when (state.state) {
            Login.DataState.State.LOADING -> LoadingScreen()
            Login.DataState.State.SUCCESS ->
                LoginScreen(
                    state = state,
                    onAction = onAction,
                )
        }
    }

    @Composable
    private fun LoginScreen(
        state: Login.DataState,
        onAction: (Login.Action) -> Unit,
    ) {
        AdminScaffold(
            actionButton = {
                LoadingButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(R.string.action_login_enter),
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
                        )
                        .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AdminAsyncImage(
                    imageData =
                        ImageData.LocalId(
                            R.drawable.logo,
                        ),
                    modifier =
                        Modifier
                            .padding(top = 24.dp)
                            .size(250.dp)
                            .haloGlowAnimated(
                                color = Color(0xFF494848),
                            ),
                    contentDescription = R.string.description_logo,
                )

                AdminTextField(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp),
                    labelText = stringResource(R.string.hint_login_login),
                    value = state.username,
                    onValueChange = { name ->
                        onAction(Login.Action.ChangeUsername(name))
                    },
                    leadingIcon = {
                        Icon(
                            painter =
                                painterResource(R.drawable.ic_person),
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
                    labelText = stringResource(R.string.hint_login_password),
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
                                painterResource(R.drawable.ic_lock),
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
                                        painterResource(R.drawable.ic_invisible)
                                    } else {
                                        painterResource(R.drawable.ic_visible)
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
}
