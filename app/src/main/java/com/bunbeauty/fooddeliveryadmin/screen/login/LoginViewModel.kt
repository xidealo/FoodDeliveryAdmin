package com.bunbeauty.fooddeliveryadmin.screen.login

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.ApiResult
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import com.bunbeauty.fooddeliveryadmin.BuildConfig
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
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
            showWrongDataError()
            return
        }
        if (!isCorrectPassword(processedPassword)) {
            showWrongDataError()
            return
        }

        viewModelScope.launch(Dispatchers.Default) {
            when (val result = userAuthorizationRepo.login(username, password)) {
                is ApiResult.Success -> {
                    result.data.let { (token, managerCityUuid, companyUuid) ->
                        dataStoreRepo.saveManagerCity(managerCityUuid)
                        dataStoreRepo.saveCompanyUuid(companyUuid)
                        dataStoreRepo.saveToken(token)
                    }
                }
                is ApiResult.Error -> {
                    showWrongDataError()
                }
            }
        }
    }

    fun subscribeOnToken() {
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
                    oldState.copy(events = oldState.events + LoginViewState.Event.OpenOrderListEvent)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun showWrongDataError() {
        mutableLoginViewState.update { oldState ->
            oldState.copy(
                isLoading = false,
                events = oldState.events + LoginViewState.Event.ShowLoginError
            )
        }
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
                events = loginState.events - events.toSet()
            )
        }
    }

    /**
     * For test login
     */
    fun clear() {
        viewModelScope.launch(IO) {
            dataStoreRepo.clearCache()
        }
    }
}