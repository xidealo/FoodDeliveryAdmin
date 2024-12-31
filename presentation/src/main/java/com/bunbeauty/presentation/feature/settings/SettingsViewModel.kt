package com.bunbeauty.presentation.feature.settings

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.profile.GetIsUnlimitedNotificationUseCase
import com.bunbeauty.domain.feature.profile.UpdateIsUnlimitedNotificationUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
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
                updateIsUnlimitedNotification(isEnabled = isChecked)
                mutableIsUnlimitedNotifications.update {
                    isChecked
                }
            }
        )
    }
}
