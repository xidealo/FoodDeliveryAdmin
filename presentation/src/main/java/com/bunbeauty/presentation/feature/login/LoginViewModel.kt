package com.bunbeauty.presentation.feature.login

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.login.CheckAuthorizationUseCase
import com.bunbeauty.domain.feature.login.LoginUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val checkAuthorizationUseCase: CheckAuthorizationUseCase,
    private val loginUseCase: LoginUseCase,
) : BaseStateViewModel<Login.DataState, Login.Action, Login.Event>(
    initState = Login.DataState(
        state = Login.DataState.State.LOADING,
        password = "",
        username = ""
    )
) {

    init {
        checkAuthorization()
    }

    override fun reduce(
        action: Login.Action,
        dataState: Login.DataState
    ) {
        when (action) {
            is Login.Action.LoginClick -> {
                login(
                    username = dataState.username,
                    password = dataState.password
                )
            }

            is Login.Action.ChangeLogin -> TODO()
            is Login.Action.ChangePassword -> TODO()
        }
    }

    fun login(
        username: String,
        password: String,
    ) {
        setState {
            copy(state = Login.DataState.State.LOADING)
        }

        val processedUsername = username.lowercase().trim()
        val processedPassword = password.lowercase().trim()
        if (!isCorrectUsername(processedUsername)) {
            sendEvent {
                Login.Event.ShowWrongCredentialError
            }
            return
        }
        if (!isCorrectPassword(processedPassword)) {
            sendEvent {
                Login.Event.ShowWrongCredentialError
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
                sendEvent {
                    Login.Event.ShowWrongCredentialError
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

    private fun isCorrectUsername(username: String): Boolean = username.isNotEmpty()

    private fun isCorrectPassword(password: String): Boolean = password.isNotEmpty()
}