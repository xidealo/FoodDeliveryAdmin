package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.ViewModel
import androidx.navigation.NavArgs
import com.bunbeauty.fooddeliveryadmin.Router
import com.bunbeauty.fooddeliveryadmin.ui.ErrorEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var router: Router

    protected val mutableMessage = MutableSharedFlow<String>(0)
    val message: SharedFlow<String> = mutableMessage.asSharedFlow()

    protected val mutableError = MutableSharedFlow<ErrorEvent>(0)
    val error: SharedFlow<ErrorEvent> = mutableError.asSharedFlow()

    fun goBack() {
        router.navigateUp()
    }
}