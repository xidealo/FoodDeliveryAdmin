package com.bunbeauty.fooddeliveryadmin.presentation.login

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.ui.fragments.login.LoginFragmentDirections.toOrdersFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiRepo: ApiRepo,
    private val dataStoreRepo: DataStoreRepo
) : BaseViewModel() {

    val loginState: StateFlow<State<String>>
        get() = _loginState
    private val _loginState = MutableStateFlow<State<String>>(State.Loading())

    fun startCheckingToken() {
        dataStoreRepo.token.onEach { token ->
            if (token.isEmpty()) {
                apiRepo.unsubscribeOnNotification()
                _loginState.value = State.Empty()
            } else {
                apiRepo.subscribeOnNotification()
                router.navigate(toOrdersFragment())
            }
        }.launchIn(viewModelScope)
    }

    fun login(username: String, password: String) {
        _loginState.value = State.Loading()

        val processedUsername = username.toLowerCase(Locale.ROOT).trim()
        val processedPassword = password.toLowerCase(Locale.ROOT).trim()
        if (!isCorrectUsername(processedUsername)) {
            _loginState.value = State.Error("Неверный логин или пароль")
        }
        if (!isCorrectPassword(processedPassword)) {
            _loginState.value = State.Error("Неверный логин или пароль")
        }
        val passwordHash = getMd5(processedPassword)

        apiRepo.login(processedUsername, passwordHash).onEach { isLoginSuccess ->
            if (isLoginSuccess) {
                dataStoreRepo.saveToken(UUID.randomUUID().toString())
            } else {
                _loginState.value = State.Error("Неверный логин или пароль")
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