package com.bunbeauty.fooddeliveryadmin.screen.settings

import android.content.res.Resources
import com.bunbeauty.domain.feature.settings.model.UserRole
import com.bunbeauty.fooddeliveryadmin.R
import javax.inject.Inject

class SettingsMapper @Inject constructor(private val resources: Resources) {

    fun mapUserRole(role: UserRole): String {
        val roleResId = when(role) {
            UserRole.MANAGER -> (R.string.hint_settings_manager)
            UserRole.ADMIN -> (R.string.hint_settings_admin)
        }
        return resources.getString(roleResId)
    }

}