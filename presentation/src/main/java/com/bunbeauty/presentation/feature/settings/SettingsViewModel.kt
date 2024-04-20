package com.bunbeauty.presentation.feature.settings

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.profile.GetIsUnlimitedNotificationUseCase
import com.bunbeauty.domain.feature.profile.UpdateIsUnlimitedNotificationUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getIsUnlimitedNotification: GetIsUnlimitedNotificationUseCase,
    private val updateIsUnlimitedNotification: UpdateIsUnlimitedNotificationUseCase
) : BaseViewModel() {

    private val mutableIsUnlimitedNotifications = MutableStateFlow<Boolean?>(null)
    val isUnlimitedNotifications = mutableIsUnlimitedNotifications.asStateFlow()

    fun updateData() {
        viewModelScope.launchSafe(
            onError = {
                // No errors
            },
            block = {
                mutableIsUnlimitedNotifications.update {
                    getIsUnlimitedNotification()
                }
            }
        )
    }

    fun onUnlimitedNotificationsCheckChanged(isChecked: Boolean) {
        viewModelScope.launchSafe(
            onError = {
                // No errors
            },
            block = {
                updateIsUnlimitedNotification(isChecked)
                mutableIsUnlimitedNotifications.update {
                    isChecked
                }
            }
        )
    }
}
