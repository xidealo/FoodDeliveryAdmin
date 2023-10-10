package com.bunbeauty.fooddeliveryadmin.coreui

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.LayoutComposeBinding
import com.bunbeauty.presentation.viewmodel.base.Action
import com.bunbeauty.presentation.viewmodel.base.Event
import com.bunbeauty.presentation.viewmodel.base.DataState
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bunbeauty.fooddeliveryadmin.compose.setContentWithTheme
import com.bunbeauty.presentation.viewmodel.base.ViewState

abstract class BaseComposeFragment<DS : DataState, VS : ViewState, A : Action, E : Event> :
    Fragment(R.layout.layout_compose) {

    abstract val viewModel: BaseStateViewModel<DS, A, E>

    private val viewBinding by viewBinding(LayoutComposeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.root.setContentWithTheme {
            val state by viewModel.state.collectAsStateWithLifecycle()
            Screen(
                state = mapState(state),
                onAction = viewModel::handleAction
            )

            val events by viewModel.events.collectAsStateWithLifecycle()
            LaunchedEffect(events) {
                events.forEach { event ->
                    handleEvent(event)
                }
                viewModel.consumeEvents(events)
            }
        }
    }

    abstract fun handleEvent(event: E)

    abstract fun mapState(state: DS): VS

    @Composable
    abstract fun Screen(state: VS, onAction: (A) -> Unit)

}