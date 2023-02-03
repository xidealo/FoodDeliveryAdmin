package com.bunbeauty.fooddeliveryadmin.navigation

import android.content.res.Resources
import androidx.fragment.app.FragmentManager
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option
import com.bunbeauty.fooddeliveryadmin.screen.option_list.OptionListBottomSheet
import com.bunbeauty.fooddeliveryadmin.screen.order_list.LogoutOption
import javax.inject.Inject

class Navigator @Inject constructor(
    private val resources: Resources
) {

    suspend fun openLogout(fragmentManager: FragmentManager): String? {
        return OptionListBottomSheet.show(
            fragmentManager = fragmentManager,
            title = resources.getString(R.string.title_logout),
            options = listOf(
                Option(
                    id = LogoutOption.LOGOUT.name,
                    title = resources.getString(R.string.action_logout),
                    isPrimary = true
                ),
                Option(
                    id = LogoutOption.CANCEL.name,
                    title = resources.getString(R.string.action_cancel)
                )
            ),
            isCenter = true
        )?.value
    }
}