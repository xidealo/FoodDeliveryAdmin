package com.bunbeauty.presentation.view_model

import androidx.databinding.ObservableField
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.domain.BuildConfig.APP_ID
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val apiRepository: IApiRepository,
    private val iDataStoreHelper: IDataStoreHelper
) : BaseViewModel() {

    val isLoading = ObservableField(true)
    val tokenLiveData = iDataStoreHelper.token.asLiveData()

    /**
     * For test login
     */
    fun clear() {
        viewModelScope.launch {
            iDataStoreHelper.clearCache()
        }
    }

    fun updateToken() {
        apiRepository.updateToken(APP_ID)
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

    fun login(login: String, password: String): SharedFlow<Boolean> {
        val passwordHash = getMd5(password)

        return apiRepository.login(login, passwordHash)
    }

    private fun getMd5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray()))
            .toString(16)
            .padStart(32, '0')
    }
}