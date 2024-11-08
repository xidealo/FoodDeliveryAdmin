package com.bunbeauty.fooddeliveryadmin.screen.login

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.login.CheckAuthorizationUseCase
import com.bunbeauty.domain.feature.login.LoginUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkAuthorizationUseCase: CheckAuthorizationUseCase,
    private val loginUseCase: LoginUseCase,
) : BaseViewModel() {

    private val mutableLoginViewState = MutableStateFlow(LoginViewState())
    val loginViewState = mutableLoginViewState.asStateFlow()

    init {
        checkAuthorization()
    }

    fun login(username: String, password: String) {
        mutableLoginViewState.update { oldState ->
            oldState.copy(isLoading = true)
        }

        val processedUsername = username.lowercase().trim()
        val processedPassword = password.lowercase().trim()
        if (!isCorrectUsername(processedUsername)) {
            mutableLoginViewState.update { oldState ->
                oldState.copy(isLoading = false) + LoginViewState.Event.ShowWrongCredentialError
            }
            return
        }
        if (!isCorrectPassword(processedPassword)) {
            mutableLoginViewState.update { oldState ->
                oldState.copy(isLoading = false) + LoginViewState.Event.ShowWrongCredentialError
            }
            return
        }

        viewModelScope.launchSafe(
            block = {
                loginUseCase(
                    username = processedUsername,
                    password = processedPassword
                )
                mutableLoginViewState.update { oldState ->
                    oldState.copy(eventList = oldState.eventList + LoginViewState.Event.OpenOrderListEvent)
                }
            },
            onError = {
                mutableLoginViewState.update { oldState ->
                    oldState.copy(isLoading = false) + LoginViewState.Event.ShowWrongCredentialError
                }
            }
        )
    }

    private fun checkAuthorization() {
        viewModelScope.launchSafe(
            block = {
                if (checkAuthorizationUseCase()) {
                    mutableLoginViewState.update { oldState ->
                        oldState.copy(eventList = oldState.eventList + LoginViewState.Event.OpenOrderListEvent)
                    }
                } else {
                    mutableLoginViewState.update { oldState ->
                        oldState.copy(isLoading = false)
                    }
                }
            },
            onError = {
                // No errors
            }
        )
    }

    private fun isCorrectUsername(username: String): Boolean {
        return username.isNotEmpty()
    }

    private fun isCorrectPassword(password: String): Boolean {
        return password.isNotEmpty()
    }

    fun consumeEvents(events: List<LoginViewState.Event>) {
        mutableLoginViewState.update { loginState ->
            loginState.copy(
                eventList = loginState.eventList - events.toSet()
            )
        }
    }
}
