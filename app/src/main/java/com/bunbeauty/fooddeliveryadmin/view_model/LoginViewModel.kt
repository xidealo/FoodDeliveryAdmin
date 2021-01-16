package com.bunbeauty.fooddeliveryadmin.view_model

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bunbeauty.fooddeliveryadmin.BuildConfig
import com.bunbeauty.fooddeliveryadmin.BuildConfig.APP_ID
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.data.api.firebase.IApiRepository
import com.bunbeauty.fooddeliveryadmin.data.local.storage.IDataStoreHelper
import com.bunbeauty.fooddeliveryadmin.data.model.Company
import com.bunbeauty.fooddeliveryadmin.ui.log_in.LoginNavigator
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val apiRepository: IApiRepository,
    private val iDataStoreHelper: IDataStoreHelper
) : BaseViewModel<LoginNavigator>() {
    override var navigator: WeakReference<LoginNavigator>? = null

    val isLoading = ObservableField(true)
    val tokenLiveData = iDataStoreHelper.token.asLiveData()

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

    fun login(login: String, password: String): LiveData<Boolean> {
        val passwordHash = getMd5(password)

        return apiRepository.login(login, passwordHash)
    }

    fun loginClick() {
        navigator?.get()?.login()
    }

    private fun getMd5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray()))
            .toString(16)
            .padStart(32, '0')
    }
}