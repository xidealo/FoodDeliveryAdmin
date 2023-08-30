package com.bunbeauty.fooddeliveryadmin.screen.settings

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.card.SwitcherCard
import com.bunbeauty.fooddeliveryadmin.compose.setContentWithTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.LayoutComposeBinding
import com.bunbeauty.presentation.feature.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<LayoutComposeBinding>() {

    override val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.updateData()

        binding.root.setContentWithTheme {
            val isUnlimitedNotifications by viewModel.isUnlimitedNotifications.collectAsStateWithLifecycle()
            SettingsScreen(
                isUnlimitedNotifications = isUnlimitedNotifications,
                onUnlimitedNotificationsCheckChanged = viewModel::onUnlimitedNotificationsCheckChanged
            )
        }
    }

    @Composable
    private fun SettingsScreen(
        isUnlimitedNotifications: Boolean?,
        onUnlimitedNotificationsCheckChanged: (Boolean) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_settings),
            backActionClick = { findNavController().popBackStack() }
        ) {
            if (isUnlimitedNotifications != null) {
                SwitcherCard(
                    modifier = Modifier.padding(16.dp),
                    labelStringId = R.string.msg_settings_unlimited_notifications,
                    checked = isUnlimitedNotifications,
                    onCheckChanged = onUnlimitedNotificationsCheckChanged
                )
            }
        }
    }
}
