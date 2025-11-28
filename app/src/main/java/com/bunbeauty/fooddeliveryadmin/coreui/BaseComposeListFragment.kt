package com.bunbeauty.fooddeliveryadmin.coreui

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.setContentWithTheme
import com.bunbeauty.fooddeliveryadmin.databinding.LayoutComposeBinding
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.coroutines.CoroutineScope

abstract class BaseComposeListFragment<DS : BaseDataState, VS : BaseViewState, A : BaseAction, E : BaseEvent> :
    Fragment(R.layout.layout_compose) {

    abstract val viewModel: BaseStateViewModel<DS, A, E>

    private val viewBinding by viewBinding(LayoutComposeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.root.setContentWithTheme {
            val state by viewModel.state.collectAsStateWithLifecycle()
            val onAction = remember {
                { action: A ->
                    viewModel.onAction(action)
                }
            }
            val lazyListState = rememberLazyListState()
            val scope = rememberCoroutineScope()

            Screen(
                state = mapState(state),
                onAction = onAction,
                lazyListState = lazyListState
            )

            val events by viewModel.events.collectAsStateWithLifecycle()
            LaunchedEffect(events) {
                events.forEach { event ->
                    handleEvent(event, lazyListState = lazyListState, coroutineScope = scope)
                }
                viewModel.consumeEvents(events)
            }
        }
    }

    abstract fun handleEvent(event: E, lazyListState: LazyListState, coroutineScope: CoroutineScope)

    @Composable
    abstract fun mapState(state: DS): VS

    @Composable
    abstract fun Screen(state: VS, lazyListState: LazyListState, onAction: (A) -> Unit)
}
