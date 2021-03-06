package com.bunbeauty.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bunbeauty.presentation.model.FieldError
import com.bunbeauty.presentation.navigation_event.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val mutableNavigation = MutableSharedFlow<NavigationEvent>(0)
    val navigation: SharedFlow<NavigationEvent> = mutableNavigation.asSharedFlow()

    private val mutableMessage = MutableSharedFlow<String>(0)
    val message: SharedFlow<String> = mutableMessage.asSharedFlow()

    private val mutableError = MutableSharedFlow<String>(0)
    val error: SharedFlow<String> = mutableError.asSharedFlow()

    private val mutableFieldError = MutableSharedFlow<FieldError>(0)
    val fieldError: SharedFlow<FieldError> = mutableFieldError.asSharedFlow()

    protected fun goTo(navigationEvent: NavigationEvent) {
        viewModelScope.launch {
            mutableNavigation.emit(navigationEvent)
        }
    }

    fun goBack() {
        viewModelScope.launch {
            mutableNavigation.emit(NavigationEvent.Back)
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            mutableMessage.emit(message)
        }
    }

    fun sendError(error: String) {
        viewModelScope.launch {
            mutableError.emit(error)
        }
    }

    fun sendFieldError(fieldKey: String, error: String) {
        viewModelScope.launch {
            mutableFieldError.emit(FieldError(fieldKey, error))
        }
    }
}