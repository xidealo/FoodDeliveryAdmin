package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.fooddeliveryadmin.ui.fragments.login.LoginFragmentDirections.toOrdersFragment
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

abstract class LoginViewModel : BaseViewModel() {

    abstract val loginState: StateFlow<State<String>>

    abstract fun startCheckingToken()
    abstract fun login(username: String, password: String)
}

class LoginViewModelImpl @Inject constructor(
    private val apiRepo: ApiRepo,
    private val dataStoreRepo: DataStoreRepo
) : LoginViewModel() {

    override val loginState = MutableStateFlow<State<String>>(State.Loading())

    override fun startCheckingToken() {
        dataStoreRepo.token.onEach { token ->
            if (token.isEmpty()) {
                apiRepo.unsubscribeOnNotification()
                loginState.value = State.Empty()
            } else {
                apiRepo.subscribeOnNotification()
                router.navigate(toOrdersFragment())
            }
        }.launchIn(viewModelScope)
    }

    override fun login(username: String, password: String) {
        loginState.value = State.Loading()

        val processedUsername = username.toLowerCase(Locale.ROOT).trim()
        val processedPassword = password.toLowerCase(Locale.ROOT).trim()
        if (!isCorrectUsername(processedUsername)) {
            loginState.value = State.Error("Неверный логин или пароль")
        }
        if (!isCorrectPassword(processedPassword)) {
            loginState.value = State.Error("Неверный логин или пароль")
        }
        val passwordHash = getMd5(processedPassword)

        apiRepo.login(processedUsername, passwordHash).onEach { isLoginSuccess ->
            if (isLoginSuccess) {
                dataStoreRepo.saveToken(UUID.randomUUID().toString())
            } else {
                loginState.value = State.Error("Неверный логин или пароль")
            }
        }.launchIn(viewModelScope)
    }

    fun isCorrectUsername(username: String): Boolean {
        if (username.isEmpty()) {
            return false
        }
        return true
    }

    fun isCorrectPassword(password: String): Boolean {
        if (password.isEmpty()) {
            return false
        }
        return true
    }

    fun getMd5(input: String): String {
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