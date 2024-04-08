package com.bunbeauty.fooddeliveryadmin.screen.profile

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.MainButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationIconCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.TextWithHintCard
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.setContentWithTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.LayoutComposeBinding
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.fooddeliveryadmin.screen.logout.LogoutBottomSheet
import com.bunbeauty.fooddeliveryadmin.screen.profile.ProfileFragmentDirections.Companion.toCafeListFragment
import com.bunbeauty.fooddeliveryadmin.screen.profile.ProfileFragmentDirections.Companion.toLoginFragment
import com.bunbeauty.fooddeliveryadmin.screen.profile.ProfileFragmentDirections.Companion.toSettingsFragment
import com.bunbeauty.fooddeliveryadmin.screen.profile.ProfileFragmentDirections.Companion.toStatisticFragment
import com.bunbeauty.presentation.feature.profile.ProfileEvent
import com.bunbeauty.presentation.feature.profile.ProfileUiState
import com.bunbeauty.presentation.feature.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<LayoutComposeBinding>() {

    override val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var profileMapper: ProfileMapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.updateData()

        binding.root.setContentWithTheme {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ProfileScreen(
                uiState = uiState,
                onSettingsClicked = viewModel::onSettingsClicked,
                onCafesClicked = viewModel::onCafesClicked,
                onStatisticClicked = viewModel::onStatisticClicked,
                onLogoutClicked = viewModel::onLogoutClick,
                onRetryClicked = viewModel::updateData
            )

            LaunchedEffect(uiState.eventList) {
                handleEventList(uiState.eventList)
            }
        }
    }

    @Composable
    private fun ProfileScreen(
        uiState: ProfileUiState,
        onCafesClicked: () -> Unit,
        onSettingsClicked: () -> Unit,
        onStatisticClicked: () -> Unit,
        onLogoutClicked: () -> Unit,
        onRetryClicked: () -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_profile),
            actionButton = {
                MainButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textStringId = R.string.action_common_logout,
                    isEnabled = uiState.isLogoutEnabled,
                    onClick = onLogoutClicked
                )
            }
        ) {
            when (val state = uiState.state) {
                ProfileUiState.State.Loading -> {
                    LoadingScreen()
                }

                ProfileUiState.State.Error -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = onRetryClicked
                    )
                }

                is ProfileUiState.State.Success -> {
                    SuccessProfileScreen(
                        uiStateSuccess = state,
                        onCafesClicked = onCafesClicked,
                        onSettingsClicked = onSettingsClicked,
                        onStatisticClicked = onStatisticClicked
                    )
                }
            }
        }
    }

    @Composable
    private fun SuccessProfileScreen(
        uiStateSuccess: ProfileUiState.State.Success,
        onCafesClicked: () -> Unit,
        onSettingsClicked: () -> Unit,
        onStatisticClicked: () -> Unit
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextWithHintCard(
                hint = profileMapper.mapUserRole(uiStateSuccess.role),
                label = uiStateSuccess.userName
            )
            NavigationIconCard(
                iconId = R.drawable.ic_cafe,
                labelStringId = R.string.action_profile_cafes,
                onClick = onCafesClicked
            )
            NavigationIconCard(
                iconId = R.drawable.ic_settings,
                labelStringId = R.string.action_profile_settings,
                onClick = onSettingsClicked
            )
            NavigationIconCard(
                iconId = R.drawable.ic_statistic,
                labelStringId = R.string.action_profile_statistic,
                onClick = onStatisticClicked
            )
        }
    }

    private fun handleEventList(
        eventList: List<ProfileEvent>
    ) {
        eventList.forEach { event ->
            when (event) {
                ProfileEvent.OpenSettingsEvent -> {
                    findNavController().navigateSafe(toSettingsFragment())
                }

                ProfileEvent.OpenCafeListEvent -> {
                    findNavController().navigateSafe(toCafeListFragment())
                }

                ProfileEvent.OpenStatisticEvent -> {
                    findNavController().navigateSafe(toStatisticFragment())
                }

                ProfileEvent.OpenLogoutEvent -> {
                    lifecycleScope.launch {
                        LogoutBottomSheet.show(parentFragmentManager)?.let { confirmed ->
                            viewModel.onLogoutConfirmed(confirmed)
                        }
                    }
                }

                ProfileEvent.OpenLoginEvent -> {
                    findNavController().navigateSafe(toLoginFragment())
                }
            }
        }
        viewModel.consumeEvents(eventList)
    }
}
