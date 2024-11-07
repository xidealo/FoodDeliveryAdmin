package com.bunbeauty.fooddeliveryadmin.screen.login

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.usecase.LoginUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    private val mutableLoginViewState: MutableStateFlow<LoginViewState> = MutableStateFlow(
        LoginViewState()
    )

    val loginViewState: StateFlow<LoginViewState> = mutableLoginViewState.asStateFlow()

    init {
        checkToken()
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

    private fun checkToken() {
        viewModelScope.launchSafe(
            block = {
                val token = dataStoreRepo.token.firstOrNull()
                if (token.isNullOrEmpty()) {
                    mutableLoginViewState.update { oldState ->
                        oldState.copy(isLoading = false)
                    }
                } else {
                    mutableLoginViewState.update { oldState ->
                        oldState.copy(eventList = oldState.eventList + LoginViewState.Event.OpenOrderListEvent)
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
