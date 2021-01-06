package com.bunbeauty.fooddeliveryadmin.view_model

import androidx.databinding.ObservableField
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bunbeauty.fooddeliveryadmin.data.api.firebase.IApiRepository
import com.bunbeauty.fooddeliveryadmin.data.local.storage.IDataStoreHelper
import com.bunbeauty.fooddeliveryadmin.ui.log_in.LoginNavigator
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    val apiRepository: IApiRepository,
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

    fun loginClick() {
        navigator?.get()?.login()
    }
}