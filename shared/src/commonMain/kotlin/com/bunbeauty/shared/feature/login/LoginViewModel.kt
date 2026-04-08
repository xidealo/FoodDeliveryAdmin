package com.bunbeauty.shared.feature.login

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.login.CheckAuthorizationUseCase
import com.bunbeauty.domain.feature.login.LoginUseCase
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel

class LoginViewModel(
    private val checkAuthorizationUseCase: CheckAuthorizationUseCase,
    private val loginUseCase: LoginUseCase,
) : BaseStateViewModel<Login.DataState, Login.Action, Login.Event>(
        initState =
            Login.DataState(
                state = Login.DataState.State.LOADING,
                password = "",
                username = "",
                isPasswordVisible = false,
                startLoginLoading = false,
            ),
    ) {
    init {
        checkAuthorization()
    }

    override fun reduce(
        action: Login.Action,
        dataState: Login.DataState,
    ) {
        when (action) {
            is Login.Action.LoginClick -> {
                login(
                    username = dataState.username,
                    password = dataState.password,
                )
            }

            is Login.Action.ChangeUsername -> changeUsername(username = action.username)
            is Login.Action.ChangePassword -> changePassword(password = action.password)
            is Login.Action.ChangeVisiblePassword -> changeVisiblePassword()
        }
    }

    fun login(
        username: String,
        password: String,
    ) {
        setState {
            copy(startLoginLoading = true)
        }

        val processedUsername = username.lowercase().trim()
        val processedPassword = password.lowercase().trim()
        if (!isCorrectUsername(processedUsername)) {
            sendEvent {
                Login.Event.ShowWrongCredentialError
            }
            setState {
                copy(startLoginLoading = false)
            }
            return
        }
        if (!isCorrectPassword(processedPassword)) {
            sendEvent {
                Login.Event.ShowWrongCredentialError
            }
            setState {
                copy(startLoginLoading = false)
            }
            return
        }

        viewModelScope.launchSafe(
            block = {
                loginUseCase(
                    username = processedUsername,
                    password = processedPassword,
                )
                sendEvent {
                    Login.Event.OpenOrderListEvent
                }
            },
            onError = {
                setState {
                    copy(startLoginLoading = false)
                }
                sendEvent {
                    Login.Event.ShowWrongLoginError
                }
            },
        )
    }

    private fun checkAuthorization() {
        viewModelScope.launchSafe(
            block = {
                if (checkAuthorizationUseCase()) {
                    sendEvent {
                        Login.Event.OpenOrderListEvent
                    }
                } else {
                    setState {
                        copy(state = Login.DataState.State.SUCCESS)
                    }
                }
            },
            onError = {
                // No errors
            },
        )
    }

    private fun changeUsername(username: String) {
        setState {
            copy(
                username = username,
            )
        }
    }

    private fun changePassword(password: String) {
        setState {
            copy(
                password = password,
            )
        }
    }

    private fun changeVisiblePassword() {
        setState {
            copy(
                isPasswordVisible = !isPasswordVisible,
            )
        }
    }

    private fun isCorrectUsername(username: String): Boolean = username.isNotEmpty()

    private fun isCorrectPassword(password: String): Boolean = password.isNotEmpty()
}
