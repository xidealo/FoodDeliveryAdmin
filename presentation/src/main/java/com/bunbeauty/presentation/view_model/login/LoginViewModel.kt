package com.bunbeauty.presentation.view_model.login

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.presentation.utils.IResourcesProvider
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.navigation_event.LoginNavigationEvent
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiRepo: ApiRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val resourcesProvider: IResourcesProvider,
) : BaseViewModel() {

    private val mutableIsLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = mutableIsLoading.asStateFlow()

    init {
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

        apiRepo.login(processedUsername, getMd5(processedPassword)).onEach { isLoginSuccess ->
            if (isLoginSuccess) {
                dataStoreRepo.saveToken(UUID.randomUUID().toString())
            } else {
                showWrongDataError()
            }
        }.launchIn(viewModelScope)
    }

    private fun subscribeOnToken() {
        dataStoreRepo.token.onEach { token ->
            if (token == null) {
                apiRepo.unsubscribeOnNotification()
                mutableIsLoading.value = false
            } else {
                apiRepo.subscribeOnNotification()
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

    private fun getMd5(input: String): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        return BigInteger(1, messageDigest.digest(input.toByteArray()))
            .toString(16)
            .padStart(32, '0')
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