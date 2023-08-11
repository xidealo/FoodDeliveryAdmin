package com.bunbeauty.fooddeliveryadmin.screen.profile

import android.content.res.Resources
import com.bunbeauty.domain.feature.profile.model.UserRole
import com.bunbeauty.fooddeliveryadmin.R
import javax.inject.Inject

class ProfileMapper @Inject constructor(private val resources: Resources) {

    fun mapUserRole(role: UserRole): String {
        val roleResId = when (role) {
            UserRole.MANAGER -> (R.string.hint_profile_manager)
            UserRole.ADMIN -> (R.string.hint_profile_admin)
        }
        return resources.getString(roleResId)
    }
}
