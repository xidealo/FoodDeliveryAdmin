package com.bunbeauty.fooddeliveryadmin.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextField
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.fooddeliveryadmin.util.Constants.IMAGE
import com.bunbeauty.presentation.feature.login.Login
import com.bunbeauty.presentation.feature.login.LoginViewModel
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.CreateMenuProduct
import com.bunbeauty.presentation.feature.settings.state.SettingsState
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
        }
    }

    @Composable
    override fun Screen(
        state: Login.DataState,
        onAction: (Login.Action) -> Unit
    ) {
        when (state.state) {
            Login.DataState.State.LOADING -> LoadingScreen()
            Login.DataState.State.SUCCESS -> LoginScreen(
                state = state,
                onAction = onAction
            )

            Login.DataState.State.ERROR -> ErrorScreen(
                mainTextId = R.string.title_common_can_not_load_data,
                extraTextId = R.string.msg_common_check_connection_and_retry,
                onClick = {
                    // todo handle
                },
            )
        }
    }

    @Composable
    private fun LoginScreen(
        state: Login.DataState,
        onAction: (Login.Action) -> Unit
    ) {
        AdminScaffold(
            actionButton = {
                LoadingButton(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.action_login_enter),
                    isLoading = false,
                    onClick = {
                        onAction(Login.Action.LoginClick)
                    },
                )
            },
            backgroundColor = AdminTheme.colors.main.surface,
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            ) {
                Spacer(Modifier.weight(1f))
                AdminTextField(
                    modifier = Modifier.fillMaxWidth(),
                    labelText = stringResource(R.string.hint_login_login),
                    value = state.username,
                    onValueChange = { name ->
                        onAction(Login.Action.ChangeLogin(name))
                    },
                    enabled = true,
                )

                AdminTextField(
                    modifier = Modifier.fillMaxWidth(),
                    labelText = stringResource(R.string.hint_login_password),
                    value = state.password,
                    onValueChange = { name ->
                        onAction(Login.Action.ChangePassword(password = name))
                    },
                    enabled = true,
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
                state = Login.DataState(
                    state = Login.DataState.State.SUCCESS, username = "", password = ""

                ),
                onAction = {},
            )
        }
    }
}
