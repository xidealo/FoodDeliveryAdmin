package com.bunbeauty.fooddeliveryadmin.screen.login

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.ApiResult
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import com.bunbeauty.fooddeliveryadmin.BuildConfig
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        subscribeOnToken()
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
                when (val result =
                    userAuthorizationRepo.login(processedUsername, processedPassword)) {
                    is ApiResult.Success -> {
                        result.data.let { (token, managerCityUuid, companyUuid) ->
                            dataStoreRepo.saveManagerCity(managerCityUuid)
                            dataStoreRepo.saveCompanyUuid(companyUuid)
                            dataStoreRepo.saveToken(token)
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

    private fun subscribeOnToken() {
        dataStoreRepo.token.onEach { token ->
            if (token.isEmpty()) {
                mutableLoginViewState.update { oldState ->
                    oldState.copy(
                        isLoading = false,
                        appVersion = BuildConfig.VERSION_NAME
                    )
                }
            } else {
                mutableLoginViewState.update { oldState ->
                    oldState.copy(eventList = oldState.eventList + LoginViewState.Event.OpenOrderListEvent)
                }
            }
        }.launchIn(viewModelScope)
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