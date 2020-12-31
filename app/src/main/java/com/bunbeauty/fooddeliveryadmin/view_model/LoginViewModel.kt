package com.bunbeauty.fooddeliveryadmin.view_model

import androidx.databinding.ObservableField
import androidx.lifecycle.asLiveData
import com.bunbeauty.fooddeliveryadmin.data.api.firebase.IApiRepository
import com.bunbeauty.fooddeliveryadmin.data.local.storage.IDataStoreHelper
import com.bunbeauty.fooddeliveryadmin.ui.log_in.LoginNavigator
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val apiRepository: IApiRepository,
    private val iDataStoreHelper: IDataStoreHelper
) : BaseViewModel<LoginNavigator>() {
    override var navigator: WeakReference<LoginNavigator>? = null

    val isLoadingButton = ObservableField(false)
    val tokenLiveData = iDataStoreHelper.token.asLiveData()

    fun login(login: String, password: String) {
        apiRepository.login(login, password)
    }

    fun isCorrectUsername(username: String): Boolean {
        if (username.isEmpty()) {
            isLoadingButton.set(false)
            return false
        }
        return true
    }

    fun isCorrectPassword(password: String): Boolean {
        if (password.isEmpty()) {
            isLoadingButton.set(false)
            return false
        }
        return true
    }

    fun loginClick() {
        navigator?.get()?.login()
    }

}