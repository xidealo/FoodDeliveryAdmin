package com.bunbeauty.fooddeliveryadmin.screen.login

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.ApiResult
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val userAuthorizationRepo: UserAuthorizationRepo,
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

        viewModelScope.launch {
            try {
                when (
                    val result =
                        userAuthorizationRepo.login(processedUsername, processedPassword)
                ) {
                    is ApiResult.Success -> {
                        result.data.let { (token, managerCityUuid, companyUuid) ->
                            dataStoreRepo.saveToken(token)
                            dataStoreRepo.saveManagerCity(managerCityUuid)
                            dataStoreRepo.saveCompanyUuid(companyUuid)
                            dataStoreRepo.saveUsername(processedUsername)
                        }
                        mutableLoginViewState.update { oldState ->
                            oldState.copy(eventList = oldState.eventList + LoginViewState.Event.OpenOrderListEvent)
                        }
                    }
                    is ApiResult.Error -> {
                        mutableLoginViewState.update { oldState ->
                            oldState.copy(isLoading = false) + LoginViewState.Event.ShowWrongCredentialError
                        }
                    }
                }
            } catch (exception: Exception) {
                mutableLoginViewState.update { oldState ->
                    oldState.copy(isLoading = false) + LoginViewState.Event.ShowConnectionError
                }
            }
        }
    }

    private fun checkToken() {
        viewModelScope.launchSafe(
            onError = {},
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
