package com.bunbeauty.fooddeliveryadmin.screen.statisticdetails

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.presentation.feature.statisticdetails.StatisticDetails
import com.bunbeauty.presentation.feature.statisticdetails.StatisticDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatisticDetailsFragment :
    SingleStateComposeFragment<StatisticDetails.DataState, StatisticDetails.Action, StatisticDetails.Event>() {
    override val viewModel: StatisticDetailsViewModel by viewModel()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAction(
            StatisticDetails.Action.Init,
        )
    }

    override fun handleEvent(event: StatisticDetails.Event) {
        when (event) {
            is StatisticDetails.Event.GoBack -> {
                findNavController().navigateUp()
            }
        }
    }

    @Composable
    override fun Screen(
        state: StatisticDetails.DataState,
        onAction: (StatisticDetails.Action) -> Unit,
    ) {
        TODO("Not yet implemented")
    }
}
