package com.bunbeauty.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bunbeauty.presentation.ErrorEvent
import com.bunbeauty.presentation.navigation_event.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val mutableNavigation = MutableSharedFlow<NavigationEvent>(0)
    val navigation: SharedFlow<NavigationEvent> = mutableNavigation.asSharedFlow()

    protected val mutableMessage = MutableSharedFlow<String>(0)
    val message: SharedFlow<String> = mutableMessage.asSharedFlow()

    protected val mutableError = MutableSharedFlow<ErrorEvent>(0)
    val error: SharedFlow<ErrorEvent> = mutableError.asSharedFlow()

    fun goBack() {
        viewModelScope.launch {
            mutableNavigation.emit(NavigationEvent.Back)
        }
    }

    fun goTo(navigationEvent: NavigationEvent) {
        viewModelScope.launch {
            mutableNavigation.emit(navigationEvent)
        }
    }

    fun sendError(error: String) {
        viewModelScope.launch {
            mutableError.emit(ErrorEvent.MessageError(error))
        }
    }
}