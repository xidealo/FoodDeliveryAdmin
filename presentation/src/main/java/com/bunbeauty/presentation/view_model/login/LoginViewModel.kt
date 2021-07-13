package com.bunbeauty.presentation.view_model.login

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.util.resources.IResourcesProvider
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.navigation_event.LoginNavigationEvent
import com.bunbeauty.presentation.state.State
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

    private val mutableLoginState: MutableStateFlow<State<String>> =
        MutableStateFlow(State.Loading())
    val loginState: StateFlow<State<String>> = mutableLoginState.asStateFlow()

    fun startCheckingToken() {
        dataStoreRepo.token.onEach { token ->
            if (token.isEmpty()) {
                apiRepo.unsubscribeOnNotification()
                mutableLoginState.value = State.Empty()
            } else {
                apiRepo.subscribeOnNotification()
                goTo(LoginNavigationEvent.ToOrders)
            }
        }.launchIn(viewModelScope)
    }

    fun login(username: String, password: String) {
        mutableLoginState.value = State.Loading()

        val processedUsername = username.toLowerCase(Locale.ROOT).trim()
        val processedPassword = password.toLowerCase(Locale.ROOT).trim()
        if (!isCorrectUsername(processedUsername)) {
            sendError(resourcesProvider.getString(R.string.error_login_wrong_data))
        }
        if (!isCorrectPassword(processedPassword)) {
            sendError(resourcesProvider.getString(R.string.error_login_wrong_data))
        }
        val passwordHash = getMd5(processedPassword)

        apiRepo.login(processedUsername, passwordHash).onEach { isLoginSuccess ->
            if (isLoginSuccess) {
                dataStoreRepo.saveToken(UUID.randomUUID().toString())
            } else {
                sendError(resourcesProvider.getString(R.string.error_login_wrong_data))
            }
        }.launchIn(viewModelScope)
    }

    private fun isCorrectUsername(username: String): Boolean {
        if (username.isEmpty()) {
            return false
        }
        return true
    }

    private fun isCorrectPassword(password: String): Boolean {
        if (password.isEmpty()) {
            return false
        }
        return true
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