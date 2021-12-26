package com.bunbeauty.presentation.view_model.login

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.ApiResult
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.navigation_event.LoginNavigationEvent
import com.bunbeauty.presentation.utils.IResourcesProvider
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
    private val resourcesProvider: IResourcesProvider,
    private val userAuthorizationRepo: UserAuthorizationRepo,
) : BaseViewModel() {

    private val mutableIsLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = mutableIsLoading.asStateFlow()

    init {
        //clear()
        subscribeOnToken()
    }

    fun login(username: String, password: String) {
        mutableIsLoading.value = true

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
                    result.data.let { pairTokenCityUuid ->
                        dataStoreRepo.saveManagerCity(pairTokenCityUuid.second)
                        dataStoreRepo.saveToken(pairTokenCityUuid.first)
                    }
                }
                is ApiResult.Error -> {
                    showWrongDataError()
                }
            }
        }
    }

    private fun subscribeOnToken() {
        dataStoreRepo.token.onEach { token ->
            if (token.isEmpty()) {
                //apiRepo.unsubscribeOnNotification()
                mutableIsLoading.value = false
            } else {
                //apiRepo.subscribeOnNotification()
                goTo(LoginNavigationEvent.ToOrders)
            }
        }.launchIn(viewModelScope)
    }

    private fun showWrongDataError() {
        mutableIsLoading.value = false
        sendError(resourcesProvider.getString(R.string.error_login_wrong_data))
    }

    private fun isCorrectUsername(username: String): Boolean {
        return username.isNotEmpty()
    }

    private fun isCorrectPassword(password: String): Boolean {
        return password.isNotEmpty()
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